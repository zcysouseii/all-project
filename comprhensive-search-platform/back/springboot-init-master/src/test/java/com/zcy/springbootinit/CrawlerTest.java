package com.zcy.springbootinit;
import java.io.IOException;
import java.util.ArrayList;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.*;
import com.zcy.springbootinit.model.entity.Picture;
import com.zcy.springbootinit.model.entity.Post;
import com.zcy.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void fetchPictureTest() throws IOException {
        int current = 1;
        String url = "https://www.bing.com/images/search?q=世界旅游胜地&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取图片标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictureList.add(picture);
        }
    }

    @Test
    void testFetchPassage() {
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
        System.out.println(records);
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            if (!(tempRecord.get("tags") instanceof JSONNull)) {
                JSONArray tags = (JSONArray) tempRecord.get("tags");
                List<String> tagList = tags.toList(String.class);
                post.setTags(JSONUtil.toJsonStr(tagList));
            }
            post.setUserId(1L);
            postList.add(post);
        }
        System.out.println(postList);
        postService.saveBatch(postList);
    }

}
