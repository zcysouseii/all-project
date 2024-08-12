package com.example.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootinit.annotation.AuthCheck;
import com.example.springbootinit.common.BaseResponse;
import com.example.springbootinit.common.ErrorCode;
import com.example.springbootinit.common.ResultUtils;
import com.example.springbootinit.exception.BusinessException;
import com.example.springbootinit.mapper.UserInterfaceInfoMapper;
import com.example.springbootinit.model.entity.InterfaceInfo;
import com.example.springbootinit.model.entity.User;
import com.example.springbootinit.model.entity.UserInterfaceInfo;
import com.example.springbootinit.model.vo.AnalysisInterfaceInfoVO;
import com.example.springbootinit.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 *
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<AnalysisInterfaceInfoVO>> getTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> userInterfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", userInterfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<AnalysisInterfaceInfoVO> collect = list.stream().map(interfaceInfo -> {
            AnalysisInterfaceInfoVO analysisInterfaceInfoVO = new AnalysisInterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, analysisInterfaceInfoVO);
            int totalNum = userInterfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            analysisInterfaceInfoVO.setTotalNum(totalNum);
            return analysisInterfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

}
