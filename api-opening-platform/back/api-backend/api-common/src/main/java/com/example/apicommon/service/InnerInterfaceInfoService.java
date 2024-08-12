package com.example.apicommon.service;

import com.example.apicommon.entity.InnerInterfaceInfo;

/**
* @author 曾成毅
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-05-17 15:40:09
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询该接口是否存在（根据请求路径、请求方法等）
     *
     * @param path
     * @param method
     * @return
     */
    InnerInterfaceInfo getInterfaceInfo(String path, String method);

}
