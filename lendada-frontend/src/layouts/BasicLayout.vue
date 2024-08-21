<template>
  <div id="basicLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <GobalHeader />
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

<script lang="ts" setup>
import GobalHeader from "@/components/GobalHeader.vue";
</script>

<style scoped>
#basicLayout {
}

#basicLayout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}

#basicLayout .content {
  box-sizing: border-box;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto 28px;
  padding: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
}

.footer {
  padding: 16px;
  text-align: center;
  background: #efefef;
}
</style>
