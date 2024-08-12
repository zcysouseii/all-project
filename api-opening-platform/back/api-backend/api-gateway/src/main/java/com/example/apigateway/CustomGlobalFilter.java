package com.example.apigateway;

import com.example.apiclientsdk.model.User;
import com.example.apiclientsdk.utils.SignUtils;
import com.example.apicommon.entity.InnerInterfaceInfo;
import com.example.apicommon.entity.InnerUser;
import com.example.apicommon.service.InnerInterfaceInfoService;
import com.example.apicommon.service.InnerUserInterfaceInfoService;
import com.example.apicommon.service.InnerUserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath().value());
        String method = request.getMethod().toString();
        log.info("请求方法：" + method);
        log.info("GET请求的请求参数：" + request.getQueryParams());
        String requestBodyStr = HttpMethod.POST.equals(request.getMethod()) ? resolveBodyFromRequest(request) : null;
        log.info("POST请求的请求参数：" + requestBodyStr);
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());

        // 2. 访问控制 - 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }

        // 3. ak / sk 鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        // 下面这个body实际上有大问题，你在请求头中写入中文是乱码的，所以建议用请求参数中的body
        String body = headers.getFirst("body");
        // get方式的body
        String getCorrectBody = request.getQueryParams().toString();
        // post方式的body
        Gson gson = new Gson();
        User user = gson.fromJson(requestBodyStr, User.class);
        String postCorrectBody = gson.toJson(user);

        InnerUser invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }
        catch (Exception e) {
            log.error("getInvokerUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }

        String secretKey = invokeUser.getSecretKey();
        String serverSign = "";
        if (method.equals("GET")) {
            serverSign = SignUtils.getSign(getCorrectBody, secretKey);
        } else if (method.equals("POST")) {
            serverSign = SignUtils.getSign(postCorrectBody, secretKey);
        }
        if (sign == null || !serverSign.equals(sign)) {
            return handleNoAuth(response);
        }

        // 这次插入一下，判断随机数和时间正确与否，但是写的不好
        // 这里为什么要校验>10000呢？因为客户端写的随机数生成只有4位
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        // 请求时间和当前时间不能超过5分钟
        // System.currentTimeMillis()获取到的并不是当前时间，是一个1970年1月1日0点到现在的毫秒数，/1000获取到的是秒数
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if (currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }

        // 4. 判断请求的模拟接口是否存在？
        InnerInterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(INTERFACE_HOST + request.getPath().value(), method);
        }
        catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        //todo 在这里要加入一个对用户调用次数的判断

        // 5. 请求转发，调用模拟接口 + 响应日志
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    @Override
    public int getOrder() {
        return -2;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        // 先得到原始的 request 和 response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 数据缓存
        DataBufferFactory bufferFactory = response.bufferFactory();
        // response 装饰器
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            // 下面的参数body就是响应的返回体
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // 判断响应的对象是否是Flux
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {//解决返回体分段传输
                        StringBuffer stringBuffer = new StringBuffer();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);
                            try {
                                stringBuffer.append(new String(content, StandardCharsets.UTF_8));
                            }
                            catch (Exception e) {
                                log.error("--list.add--error", e);
                            }
                        });
                        String result = stringBuffer.toString();
                        // result就是response的值，想修改、查看就随意而为了
                        // 拿到result之后就判断调用成功还是失败
                        // todo 调用成功就count + 1，调用失败就返回原响应
                        try {
                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                        }
                        catch (Exception e) {
                            log.error("invokeCount error", e);
                        }
                        Map<String, String> urlParams = request.getQueryParams().toSingleValueMap();

                        String requestBodyStr = resolveBodyFromRequest(request);
                        log.info("\n请求地址:【{}】\n请求参数:GET【{}】|POST:【{}】,\n响应数据:【{}】", request.getURI(), urlParams, requestBodyStr, result);

                        byte[] uppedContent = new String(result.getBytes(), StandardCharsets.UTF_8).getBytes();
                        response.getHeaders().setContentLength(uppedContent.length);
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

}