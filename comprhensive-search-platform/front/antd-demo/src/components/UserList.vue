<template>
  <a-list item-layout="horizontal" :data-source="props.userList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-card hoverable style="width: 240px">
          <template #cover>
            <img alt="example" src="https://img1.baidu.com/it/u=3738863420,1873922533&fm=253&fmt=auto&app=138&f=JPEG?w=1048&h=800" />
          </template>
          <a-card-meta :title="item.userName">
            <template #description>{{item.userProfile}}</template>
          </a-card-meta>
        </a-card>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import { defineProps, withDefaults } from "vue";

interface Props {
  userList: any[];
}

const props = withDefaults(defineProps<Props>(), {
  userList: () => [],
});
</script>

<style scoped>

</style>