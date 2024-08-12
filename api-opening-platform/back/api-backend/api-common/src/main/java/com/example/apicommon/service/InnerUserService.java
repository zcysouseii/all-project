package com.example.apicommon.service;

import com.example.apicommon.entity.InnerUser;

public interface InnerUserService {

    /**
     * 从数据库中查询已经分配给用户的密钥（传入accessKey，返回一个用户的实例，然后比较secretKey）
     *
     * @param accessKey
     * @return
     */
    InnerUser getInvokeUser(String accessKey);

}
