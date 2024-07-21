<template>
  <a-list
    :data-source="props.pictureList"
    :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3 }"
    item-layout="horizontal"
  >
    <template #renderItem="{ item }">
      <a-list-item>
        <a-card hoverable>
          <template #cover>
            <img :src="item.url" alt="example" />
          </template>
          <a-card-meta :title="item.title" />
        </a-card>
      </a-list-item>
    </template>
  </a-list>
</template>

<script lang="ts" setup>
import { defineProps, withDefaults } from "vue";

interface Props {
  pictureList: any[];
}

const props = withDefaults(defineProps<Props>(), {
  pictureList: () => [],
});
</script>

<style scoped></style>
