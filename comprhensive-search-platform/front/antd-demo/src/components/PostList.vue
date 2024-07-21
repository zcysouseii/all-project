<template>
  <a-list :data-source="props.postList" item-layout="horizontal">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta :description="item.content">
          <template #title>
            <a href="https://www.antdv.com/">{{ item.title }}</a>
          </template>
          <template #avatar>
            <a-avatar
              src="https://img1.baidu.com/it/u=3738863420,1873922533&fm=253&fmt=auto&app=138&f=JPEG?w=1048&h=800"
            />
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>

<script lang="ts" setup>
import { defineProps, withDefaults } from "vue";

interface Props {
  postList: any[];
}

const props = withDefaults(defineProps<Props>(), {
  postList: () => [],
});
</script>

<style scoped></style>
