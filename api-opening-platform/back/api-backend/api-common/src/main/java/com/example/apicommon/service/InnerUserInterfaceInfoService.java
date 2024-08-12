package com.example.apicommon.service;

/**
* @author 曾成毅
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2024-06-05 22:25:41
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计（传入调用的用户ID和调用的接口ID）
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
