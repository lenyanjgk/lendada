<template>
  <div id="userLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <img class="logo" src="../assets/logo.png" />
          <div>智慧答 AI 答题应用平台</div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a
          href="https://github.com/lenyanjgk"
          style="text-decoration: none"
          target="_blank"
          >Lenyanjgk</a
        >
        &copy; zhihuida. All rights reserved.
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script lang="ts" setup></script>

<style scoped>
#userLayout {
  text-align: center;
  background: url("../assets/bg.png");
  background-size: cover;
}

#userLayout .logo {
  width: 48px;
  height: 48px;
}

#userLayout .header {
  margin-top: 16px;
  font-size: 21px;
}

#userLayout .content {
  margin-bottom: 16px;
  padding: 20px;
}

.footer {
  padding: 16px;
  text-align: center;
  background: #efefef;
}
</style>
