package com.zcy.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zcy.springbootinit.model.entity.Post;
import com.zcy.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取初始帖子列表
 */
@Component
@Slf4j
public class FetchUInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 1. 获取数据
        String url = "https://api.code-nav.cn/api/post/list/page/vo";
        String json = "{\"pageSize\":12,\"sortOrder\":\"descend\",\"sortField\":\"createTime\",\"tags\":[],\"current\":1,\"reviewStatus\":1,\"category\":\"文章\",\"hiddenContent\":true}";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        // 2. json转对象
        Map<String, Object> map = JSONUtil.toBean(result2, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("plainTextDescription"));
            if (!(tempRecord.get("tags") instanceof JSONNull)) {
                JSONArray tags = (JSONArray) tempRecord.get("tags");
                List<String> tagList = tags.toList(String.class);
                post.setTags(JSONUtil.toJsonStr(tagList));
            }
            post.setUserId(1L);
            postList.add(post);
        }
        // 3. 入库
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("获取初始化帖子列表成功，条数 = {}", postList.size());
        }
        else {
            log.error("获取初始化帖子列表失败");
        }
    }

}
