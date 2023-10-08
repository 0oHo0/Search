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
      <PostList :post-list="postList"/>
    </a-tab-pane>
    <a-tab-pane key="picture" tab="图片" force-render>
      <PictureList/>
    </a-tab-pane>
    <a-tab-pane key="user" tab="用户">
      <UserList :user-list="userList"/>
    </a-tab-pane>
  </a-tabs>
</div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRouter, useRoute } from "vue-router";
import myAxios from "@/plugins/myAxios";

const postList = ref();
myAxios.post("post/list/page/vo", {}).then((res:any) => {
  postList.value = res.records;
});

const userList = ref();
myAxios.post("user/list/page/vo", {}).then((res:any) => {
  userList.value = res.records;
});

const route = useRoute();
const activeKey = route.params.category;
const router = useRouter();
const initsearchParams = {
  text: '',
  pageSize: 10,
  pageNum:1,
}
const searchParams = ref(initsearchParams);

watchEffect(() => {
  searchParams.value = {
    ...initsearchParams,
    text: route.query.text as string,
  }; 
});

const onSearch = (value: string) => {
  alert(value);
  router.push({
    query: searchParams.value,
  });
};


const onTabeChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
