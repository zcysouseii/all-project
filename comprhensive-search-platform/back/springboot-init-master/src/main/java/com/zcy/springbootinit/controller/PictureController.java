package com.zcy.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcy.springbootinit.common.BaseResponse;
import com.zcy.springbootinit.common.ErrorCode;
import com.zcy.springbootinit.common.ResultUtils;
import com.zcy.springbootinit.exception.ThrowUtils;
import com.zcy.springbootinit.model.dto.picture.PictureQueryRequest;
import com.zcy.springbootinit.model.entity.Picture;
import com.zcy.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页搜索
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                          HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }

}
