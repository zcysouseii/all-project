package com.example.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootinit.model.entity.UserInterfaceInfo;

/**
* @author 曾成毅
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2024-06-05 22:25:41
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId, long userId);

}
