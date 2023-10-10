<template>
  <div class="index-page">
    <a-input-search v-model:value="searchParams.text" placeholder="input search text" enter-button="Search" size="large"
      @search="onSearch" />
    <MyDivider />
    <a-tabs v-model:activeKey="activeKey" @change="onTabeChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片" force-render>
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
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

const initsearchParams = {
  text: '',
  pageSize: 10,
  currnt: 1,
}
const searchParams = ref(initsearchParams);
const userList = ref();
const postList = ref();
const pictureList = ref();

const loadData = (params : any) => {
  const Params = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("search/all", Params).then((res: any) => {
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
  });
}

const route = useRoute();
const activeKey = route.params.category;
const router = useRouter();

watchEffect(() => {
  searchParams.value = {
    ...initsearchParams,
    text: route.query.text as string,
  };
});

const onSearch = (value: string) => {
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};


const onTabeChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};

loadData(searchParams);

</script>
