package com.example.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.example.apiclientsdk.model.User;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.example.apiclientsdk.utils.SignUtils.getSign;


public class ApiClient {

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.get("http://localhost:8090/api/name/", paramMap);
        return result;

    }

    public String getNameByPost(String name) {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result = HttpUtil.post("http://localhost:8090/api/name/", paramMap);
        System.out.println(result);
        return result;

    }

    private Map<String, String> getHeaderMap(String body) {
        // 参数列表
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 不能直接发送密钥，会被拦截不安全
        // hashMap.put("secretKey", secretKey);
        // 随机数，防止重放
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        // 如果想在请求头中置如中文，必须转码
        String encodedText = new String(body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        hashMap.put("body", encodedText);
        // 时间戳，防止重放
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", getSign(body, secretKey));
        return hashMap;
    }

    public String getNameByPost(User user) {

        Gson gson = new Gson();
        // user是参数，将user对象转换为json字符串
        String json = gson.toJson(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8090/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        System.out.println(body);
        return body;

    }

}
