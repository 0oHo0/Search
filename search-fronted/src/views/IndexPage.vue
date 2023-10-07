<template>
<div class="index-page">
  <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <MyDivider />
    <a-tabs v-model:activeKey="activeKey" @change="onTabeChange">
    <a-tab-pane key="post" tab="文章">
      <PostList/>
    </a-tab-pane>
    <a-tab-pane key="picture" tab="图片" force-render>
      <PictureList/>
    </a-tab-pane>
    <a-tab-pane key="user" tab="用户">
      <UserList/>
    </a-tab-pane>
  </a-tabs>
</div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRouter } from "vue-router";

const activeKey = ref('post');
const router = useRouter();
const searchParams = ref({
  text : '',
})

const onSearch = (value: string) => {
  alert(value);
  router.push({
    query: searchParams.value,
  });
};
const onTabeChange = (key: string) => {
  router.push({
    path: '/${key}',
    query: searchParams.value,
  });
};
</script>
