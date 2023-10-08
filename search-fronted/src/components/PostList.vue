<template>
    <a-list item-layout="horizontal" :data-source="props.postList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta
          :description="item.content"
        >
          <template #title>
            <a href="https://www.antdv.com/">{{ item.title }}</a>
          </template>
          <template #avatar>
            <a-avatar :src="logo" />
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from 'vue';
import logo from "@/assets/logo.png";
interface Props{
    postList: [];
}
const props = withDefaults(defineProps<Props>(), {
    postList : () => [],
});

</script>

<style scoped>

</style>