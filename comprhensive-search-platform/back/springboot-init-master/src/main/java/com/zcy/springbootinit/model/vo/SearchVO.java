package com.zcy.springbootinit.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcy.springbootinit.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索视图
 *
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private List<Object> dataList;

    private static final long serialVersionUID = 1L;

}
