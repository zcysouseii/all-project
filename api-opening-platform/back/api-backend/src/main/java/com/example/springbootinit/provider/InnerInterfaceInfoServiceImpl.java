package com.example.springbootinit.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.apicommon.entity.InnerInterfaceInfo;
import com.example.apicommon.service.InnerInterfaceInfoService;
import com.example.springbootinit.common.ErrorCode;
import com.example.springbootinit.exception.BusinessException;
import com.example.springbootinit.mapper.InterfaceInfoMapper;
import com.example.springbootinit.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InnerInterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InnerInterfaceInfo innerInterfaceInfo = new InnerInterfaceInfo();
        BeanUtils.copyProperties(interfaceInfo, innerInterfaceInfo);

        return innerInterfaceInfo;
    }

}
