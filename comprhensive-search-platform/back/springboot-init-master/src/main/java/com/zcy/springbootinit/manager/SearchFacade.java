package com.zcy.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcy.springbootinit.common.ErrorCode;
import com.zcy.springbootinit.datasource.*;
import com.zcy.springbootinit.exception.BusinessException;
import com.zcy.springbootinit.exception.ThrowUtils;
import com.zcy.springbootinit.model.dto.post.PostQueryRequest;
import com.zcy.springbootinit.model.dto.search.SearchRequest;
import com.zcy.springbootinit.model.dto.user.UserQueryRequest;
import com.zcy.springbootinit.model.entity.Picture;
import com.zcy.springbootinit.model.enums.SearchTypeEnum;
import com.zcy.springbootinit.model.vo.PostVO;
import com.zcy.springbootinit.model.vo.SearchVO;
import com.zcy.springbootinit.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private DataSourceRegister dataSourceRegister;

    public SearchVO searchAll(SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        String type = searchRequest.getType();
        if (StringUtils.isBlank(type)) {
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, 1, 10);
                return picturePage;
            });
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
                return userVOPage;
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize);
                return postVOPage;
            });
            // 等前面的三个并行任务都执行完后才执行下面代码
            CompletableFuture.allOf(userTask, postTask, pictureTask).join();

            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();

                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());

                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
            ThrowUtils.throwIf(searchTypeEnum == null, ErrorCode.PARAMS_ERROR);

            SearchVO searchVO = new SearchVO();
            DataSource dataSource = dataSourceRegister.getDataSourceByType(type);
            Page page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }

}
