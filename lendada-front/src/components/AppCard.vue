<template>
  <a-card
    :body-style="{ padding: '16px' }"
    class="appCard"
    hoverable
    @click="doCardClick"
  >
    <template #cover>
      <div :style="{ height: '184px', overflow: 'hidden' }" class="card-cover">
        <img :alt="app.appName" :src="app.appIcon" class="card-image" />
      </div>
    </template>
    <template #actions>
      <span class="icon-hover" @click="doShare">
        <IconShareInternal />
      </span>
    </template>
    <a-card-meta :description="app.appDesc" :title="app.appName">
      <template #avatar>
        <div class="avatar-container">
          <a-avatar :image-url="app.user?.userAvatar" size="small" />
          <a-typography-text class="username">
            {{ app.user?.userName ?? "无名" }}
          </a-typography-text>
        </div>
      </template>
    </a-card-meta>
  </a-card>
  <ShareModal ref="shareModalRef" :link="shareLink" title="应用分享" />
</template>

<script lang="ts" setup>
import { IconShareInternal } from "@arco-design/web-vue/es/icon";
import API from "@/api";
import { defineProps, ref, withDefaults } from "vue";
import { useRouter } from "vue-router";
import ShareModal from "@/components/ShareModal.vue";

interface Props {
  app: API.AppVO;
}

const props = withDefaults(defineProps<Props>(), {
  app: () => ({}),
});

const router = useRouter();
const doCardClick = () => {
  router.push(`/app/detail/${props.app.id}`);
};

const shareModalRef = ref();
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.app.id}`;

const doShare = (e: Event) => {
  if (shareModalRef.value) {
    shareModalRef.value.openModal();
  }
  e.stopPropagation();
};
</script>

<style scoped>
.appCard {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.appCard:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.16), 0 0 12px rgba(0, 0, 0, 0.08);
  transform: scale(1.02);
}

.card-cover {
  overflow: hidden;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-container {
  display: flex;
  align-items: center;
  color: #1d2129;
}

.username {
  margin-left: 8px;
  font-size: 14px;
}

.icon-hover {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.icon-hover:hover {
  background-color: var(--gray-2);
}
</style>
