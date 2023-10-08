<template>
    <a-list item-layout="horizontal" :data-source="props.userList">
        <template #renderItem="{ item }">
            <a-list-item>
                <a-card hoverable style="width: 240px">
                    <template #cover>
                        <img alt="example" :src="logo" />
                    </template>
                    <a-card-meta :title="item.userName">
                        <template #description>{{ item.userProfile }}</template>
                    </a-card-meta>
                </a-card>
            </a-list-item>
        </template>
    </a-list>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from 'vue';
import logo from "@/assets/logo.png";

interface Props {
    userList: [];
}
const props = withDefaults(defineProps<Props>(), {
    userList: () => [],
});

</script>

<style scoped></style>