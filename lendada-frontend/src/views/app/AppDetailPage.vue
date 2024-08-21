<template>
  <div id="appDetailPage">
    <a-card>
      <a-row style="margin-bottom: 16px">
        <a-col class="content-wrapper" flex="auto">
          <h2>{{ data.appName }}</h2>
          <p>{{ data.appDesc }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data.scoringStrategy] }}</p>
          <p>
            <a-space>
              作者：
              <div :style="{ display: 'flex', alignItems: 'center' }">
                <a-avatar
                  :image-url="data.user?.userAvatar"
                  :size="24"
                  :style="{ marginRight: '8px' }"
                />
                <a-typography-text
                  >{{ data.user?.userName ?? "无名" }}
                </a-typography-text>
              </div>
            </a-space>
          </p>
          <p>
            创建时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button :href="`/answer/do/${id}`" type="primary"
              >开始答题
            </a-button>
            <a-button @click="doShare">分享应用</a-button>
            <a-button v-if="isMy" :href="`/add/question/${id}`"
              >设置题目
            </a-button>
            <a-button v-if="isMy" :href="`/add/scoring_result/${id}`"
              >设置评分
            </a-button>
            <a-button v-if="isMy" :href="`/add/app/${id}`">修改应用</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image :src="data.appIcon" width="100%" />
        </a-col>
      </a-row>
    </a-card>
    <ShareModal ref="shareModalRef" :link="shareLink" title="应用分享" />
  </div>
</template>

<script lang="ts" setup>
import { computed, defineProps, ref, watchEffect, withDefaults } from "vue";
import API from "@/api";
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { useLoginUserStore } from "@/store/userStore";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";
import ShareModal from "@/components/ShareModal.vue";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const router = useRouter();

const data = ref<API.AppVO>({});

// 获取登录用户
const loginUserStore = useLoginUserStore();
let loginUserId = loginUserStore.loginUser?.id;
// 是否为本人创建
const isMy = computed(() => {
  return loginUserId && loginUserId === data.value.userId;
});

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0) {
    data.value = res.data.data as any;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

// 分享弹窗的引用
const shareModalRef = ref();

// 分享链接
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.id}`;

// 分享
const doShare = (e: Event) => {
  if (shareModalRef.value) {
    shareModalRef.value.openModal();
  }
  // 阻止冒泡，防止跳转到详情页
  e.stopPropagation();
};
</script>
<style scoped>
#appDetailPage {
  padding: 24px;
  background-color: #f5f5f5;
}

#appDetailPage .content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

#appDetailPage h2 {
  color: #333;
  font-size: 24px;
}

#appDetailPage p {
  color: #666;
  font-size: 16px;
  line-height: 1.5;
}

#appDetailPage .ant-btn {
  font-size: 14px;
}

#appDetailPage .ant-space {
  margin-top: 16px;
}

#appDetailPage .ant-image {
  overflow: hidden;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  #appDetailPage {
    padding: 16px;
  }

  #appDetailPage h2 {
    font-size: 20px;
  }

  #appDetailPage p {
    font-size: 14px;
  }

  #appDetailPage .ant-btn {
    font-size: 12px;
  }
}
</style>
