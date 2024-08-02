<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRoute } from "vue-router";
import { getLoginUserUsingGet } from "@/api/userController";

const route = useRoute();

import { AxiosResponse } from "axios";

// 假设这是你的方法调用，确保 getLoginUserUsingGet 返回 AxiosPromise 或 Promise<AxiosResponse>
getLoginUserUsingGet().then((res) => {
  if (res && res.data) {
    console.log(res.data);
  }
});
</script>

<style>
#app {
}
</style>
