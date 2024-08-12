package com.example.apiinterface.controller;

import com.example.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user, HttpServletRequest request) {
//        下面注释是因为已经在网关中进行校验了

//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String body = request.getHeader("body");
//        String sign = request.getHeader("sign");
//
//        // 节省时间，校验就是先不做了
//        //todo 很明显，应该根据accessKey 从数据库中查出secretKey 然后计算出签名进行比较
//        if (!accessKey.equals("zcy")) {
//            throw new RuntimeException("无权限");
//        }
//
//        // 关于nonce和timestamp防止重放攻击就先不做了，其实就是自己设置一个过期时间，nonce就只用以这个过期时间存入数据库，在这段时间内带有这个随机数的请求无效
//        // 用timestamp计算当前时间与timestamp的时间，超过一定时间过期了也没用了，不能重放
//
//        String serverSign = SignUtils.getSign(JSONUtil.toJsonStr(user), "abcdefgh");
//        if (! serverSign.equals(sign)) {
//            throw new RuntimeException("无权限");
//        }
        String result = "POST 用户名字是" + user.getUsername();
        return result;
    }

}
