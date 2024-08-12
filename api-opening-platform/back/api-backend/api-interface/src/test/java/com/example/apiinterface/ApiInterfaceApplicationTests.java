package com.example.apiinterface;

import com.example.apiclientsdk.client.ApiClient;
import com.example.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    @Resource
    private ApiClient apiClient;

    @Test
    void contextLoads() {
        String zcy = apiClient.getNameByGet("zcy");
        User user = new User();
        user.setUsername("zcy");
        String nameByPost = apiClient.getNameByPost(user);
        System.out.println(zcy);
        System.out.println(nameByPost);
    }

}
