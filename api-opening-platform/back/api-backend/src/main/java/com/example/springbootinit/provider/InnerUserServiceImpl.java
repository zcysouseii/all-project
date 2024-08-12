package com.example.springbootinit.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.apicommon.entity.InnerUser;
import com.example.apicommon.service.InnerUserService;
import com.example.springbootinit.common.ErrorCode;
import com.example.springbootinit.exception.BusinessException;
import com.example.springbootinit.mapper.UserMapper;
import com.example.springbootinit.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public InnerUser getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InnerUser innerUser = new InnerUser();
        BeanUtils.copyProperties(user, innerUser);

        return innerUser;
    }

}
