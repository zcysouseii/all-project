package com.example.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootinit.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 曾成毅
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Mapper
* @createDate 2024-06-05 22:25:41
* @Entity com.example.springbootinit.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




