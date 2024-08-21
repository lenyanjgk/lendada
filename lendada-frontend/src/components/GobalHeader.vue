<template>
  <a-row id="globalHeader" :wrap="false" align="center">
    <a-col flex="auto">
      <a-menu
        :selected-keys="selectedKeys"
        mode="horizontal"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="titleBar">
            <img class="logo" src="../assets/logo.png" />
            <div class="title">智慧答</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="150px">
      <a-space>
        <div v-if="loginUserStore.loginUser.id">
          {{ loginUserStore.loginUser.userName ?? "匿名用户" }}
          <Avatar>
            <img :src="loginUserStore.loginUser.userAvatar" alt="avatar" />
          </Avatar>
          <a-popconfirm
            content="是否登出"
            position="bottom"
            type="warning"
            @ok="logout"
          >
            <a-button class="button">登出</a-button>
          </a-popconfirm>
        </div>
        <div v-else>
          <a-button href="/user/login" type="primary">登录</a-button>
        </div>
      </a-space>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { Avatar } from "@arco-design/web-vue";
import message from "@arco-design/web-vue/es/message";
import { userLogoutUsingPost } from "@/api/userController";

const loginUserStore = useLoginUserStore();

const router = useRouter();
// 当前选中的菜单项
const selectedKeys = ref(["/"]);
// 路由跳转时，自动更新选中的菜单项
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

// 展示在菜单栏的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

// 点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const logout = async () => {
  const res = await userLogoutUsingPost();
  if (res.data.code === 0) {
    message.success("退出成功");
  } else {
    message.error("退出失败，" + res.data.message);
  }
  //刷新页面的函数
  window.location.reload();
};
</script>

<style scoped>
#globalHeader {
}

.titleBar {
  display: flex;
  align-items: center;
}

.title {
  margin-left: 16px;
  color: black;
}

.logo {
  height: 48px;
}
</style>
