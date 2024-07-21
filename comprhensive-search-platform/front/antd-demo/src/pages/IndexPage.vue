<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      enter-button="Search"
      placeholder="input search text"
      size="large"
      @search="onSearch"
    />
  </div>
  <MyDivider />
  <a-tabs v-model:activeKey="activeKey" @change="onTableChange">
    <a-tab-pane key="post" tab="文章">
      <PostList :post-list="postList" />
    </a-tab-pane>
    <a-tab-pane key="user" tab="用户">
      <UserList :user-list="userList" />
    </a-tab-pane>
    <a-tab-pane key="picture" tab="图片">
      <PictureList :picture-list="pictureList" />
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts" setup>
import PostList from "@/components/PostList.vue";
import UserList from "@/components/UserList.vue";
import PictureList from "@/components/PictureList.vue";

import { ref, watchEffect } from "vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import myAxios from "@/plungins/myAxios";
import { message } from "ant-design-vue";

const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);

/**
 * 加载数据
 * @param params
 */
const loadDataOld = (params: any) => {
  const postQuery = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("post/list/page/vo", postQuery).then((res: any) => {
    postList.value = res.records;
  });

  const userQuery = {
    ...params,
    userName: params.text,
  };
  myAxios.post("user/list/page/vo", userQuery).then((res: any) => {
    userList.value = res.records;
  });

  const pictureQuery = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("picture/search/page/vo", pictureQuery).then((res: any) => {
    pictureList.value = res.records;
  });
};
/**
 * 加载全部数据
 * @param params
 */
const loadAllData = (params: any) => {
  const query = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("search/all", query).then((res: any) => {
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
  });
};
/**
 * 加载一种数据
 * @param params
 */
const loadData = (params: any) => {
  const type = route.params.category;
  if (!type) {
    message.error("类别为空");
    return;
  }
  const query = {
    ...params,
    type: type,
    searchText: params.text,
  };
  myAxios.post("search/all", query).then((res: any) => {
    console.log(res)
    if (type === "post") {
      postList.value = res.dataList;
    } else if (type === "user") {
      userList.value = res.dataList;
    } else if (type === "picture") {
      pictureList.value = res.dataList;
    }
  });
};

const router = useRouter();
const route = useRoute();

const initSearchParams = {
  text: route.query.text,
  pageSize: 10,
  pageNum: 1,
};
const activeKey = ref(route.params.category);
const searchParams = ref(initSearchParams);

// 第一次加载数据
loadData(initSearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});

const onSearch = (value: string) => {
  console.log(value);
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};
const onTableChange = async (key: string) => {
  await router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
  loadData(searchParams.value);
};
</script>
