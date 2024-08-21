<template>
  <div id="answerResultPage">
    <a-card
      style="border-radius: 12px; box-shadow: 0 4px 22px rgba(0, 0, 0, 0.1)"
    >
      <a-row style="margin-bottom: 16px">
        <a-col class="content-wrapper" flex="auto">
          <h2 class="result-title">{{ data.resultName }}</h2>
          <p class="description" style="margin-bottom: 22px">
            {{ data.resultDesc }}
          </p>
          <p style="margin-bottom: 22px">结果 id：{{ data.resultId }}</p>
          <p style="margin-bottom: 22px">结果得分：{{ data.resultScore }}</p>
          <p style="margin-bottom: 22px">我的答案：{{ data.choices }}</p>
          <p style="margin-bottom: 22px">应用 id：{{ data.appId }}</p>
          <p style="margin-bottom: 22px">
            应用类型：{{ APP_TYPE_MAP[data.appType] }}
          </p>
          <p style="margin-bottom: 22px">
            评分策略：{{ APP_SCORING_STRATEGY_MAP[data.scoringStrategy] }}
          </p>
          <p style="margin-bottom: 22px">
            <a-space>
              答题人：
              <div :style="{ display: 'flex', alignItems: 'center' }">
                <a-avatar
                  :image-url="data.user?.userAvatar"
                  :size="24"
                  :style="{ marginRight: '22px' }"
                />
                <a-typography-text
                  >{{ data.user?.userName ?? "无名" }}
                </a-typography-text>
              </div>
            </a-space>
          </p>
          <p style="margin-bottom: 22px">
            答题时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button :href="`/answer/do/${data.appId}`" type="primary"
              >去答题
            </a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image :src="data.resultPicture" width="100%" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, ref, watchEffect, withDefaults } from "vue";
import API from "@/api";
import { getUserAnswerVoByIdUsingGet } from "@/api/userAnswerController";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "../../constant/app";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const router = useRouter();

const data = ref<API.UserAnswerVO>({});

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getUserAnswerVoByIdUsingGet({
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
</script>

<style scoped>
#answerResultPage {
  background-color: #f9f9f9;
}

.result-title {
  color: #333;
  font-weight: bold;
  font-size: 24px;
}

.description {
  color: #666;
  font-size: 16px;
}

#answerResultPage.content-wrapper > * {
  margin-bottom: 24px;
}

a-button[type="primary"] {
  background-color: #1890ff;
  border-color: #1890ff;
}

a-avatar {
  border-radius: 50%;
}
</style>
