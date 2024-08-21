<template>
  <div id="doAnswerPage">
    <a-card class="answer-page-card">
      <h1 class="answer-page-title">{{ app.appName }}</h1>
      <p class="answer-page-desc">{{ app.appDesc }}</p>
      <h2 class="answer-page-question" style="margin-bottom: 16px">
        {{ current }}、{{ currentQuestion?.title }}
      </h2>
      <div>
        <a-radio-group
          v-model="currentAnswer"
          :options="questionOptions"
          direction="vertical"
          @change="doRadioChange"
        />
      </div>
      <div class="answer-page-actions" style="margin-top: 24px">
        <a-space size="large">
          <a-button
            v-if="current < questionContent.length"
            :disabled="!currentAnswer"
            type="primary"
            @click="current += 1"
          >
            下一题
          </a-button>
          <a-button
            v-if="current === questionContent.length"
            :disabled="!currentAnswer"
            :loading="submitting"
            type="primary"
            @click="doSubmit"
          >
            {{ submitting ? "评分中" : "查看结果" }}
          </a-button>
          <a-button v-if="current > 1" @click="current -= 1"> 上一题</a-button>
        </a-space>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import {
  computed,
  defineProps,
  reactive,
  ref,
  watchEffect,
  withDefaults,
} from "vue";
import API from "@/api";
import { useRouter } from "vue-router";
import { listQuestionVoByPageUsingPost } from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";
import { getAppVoByIdUsingGet } from "@/api/appController";
import {
  addUserAnswerUsingPost,
  generateUserAnswerIdUsingGet,
} from "@/api/userAnswerController";

interface Props {
  appId: string;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const router = useRouter();

const app = ref<API.AppVO>({});
// 题目内容结构（理解为题目列表）
const questionContent = ref<API.QuestionContentDTO[]>([]);

// 当前题目的序号（从 1 开始）
const current = ref(1);
// 当前题目
const currentQuestion = ref<API.QuestionContentDTO>({});
// 当前题目选项
const questionOptions = computed(() => {
  return currentQuestion.value?.options
    ? currentQuestion.value.options.map((option) => {
        return {
          label: `${option.key}. ${option.value}`,
          value: option.key,
        };
      })
    : [];
});
// 当前答案
const currentAnswer = ref<string>();
// 回答列表
const answerList = reactive<string[]>([]);
// 是否正在提交结果
const submitting = ref(false);

// 唯一 id
const id = ref<number>();

// 生成唯一 id
const generateId = async () => {
  const res = await generateUserAnswerIdUsingGet();
  if (res.data.code === 0) {
    id.value = res.data.data as any;
  } else {
    message.error("获取唯一 id 失败，" + res.data.message);
  }
};

// 进入页面时，生成唯一 id
watchEffect(() => {
  generateId();
});

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  // 获取 app
  let res: any = await getAppVoByIdUsingGet({
    id: props.appId as any,
  });
  if (res.data.code === 0) {
    app.value = res.data.data as any;
  } else {
    message.error("获取应用失败，" + res.data.message);
  }
  // 获取题目
  res = await listQuestionVoByPageUsingPost({
    appId: props.appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });
  if (res.data.code === 0 && res.data.data?.records) {
    questionContent.value = res.data.data.records[0].questionContent;
  } else {
    message.error("获取题目失败，" + res.data.message);
  }
};

// 获取旧数据
watchEffect(() => {
  loadData();
});

// 改变 current 题号后，会自动更新当前题目和答案
watchEffect(() => {
  currentQuestion.value = questionContent.value[current.value - 1];
  currentAnswer.value = answerList[current.value - 1];
});

/**
 * 选中选项后，保存选项记录
 * @param value
 */
const doRadioChange = (value: string) => {
  answerList[current.value - 1] = value;
};

/**
 * 提交
 */
const doSubmit = async () => {
  if (!props.appId || !answerList) {
    return;
  }
  submitting.value = true;
  const res = await addUserAnswerUsingPost({
    appId: props.appId as any,
    choices: answerList,
    id: id.value as any,
  });
  if (res.data.code === 0 && res.data.data) {
    router.push(`/answer/result/${res.data.data}`);
  } else {
    message.error("提交答案失败，" + res.data.message);
  }
  submitting.value = false;
};
</script>

<style scoped>
.answer-page-card {
  max-width: 600px;
  margin: 24px auto;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.answer-page-title {
  margin-bottom: 8px;
  font-size: 24px;
}

.answer-page-desc {
  margin-bottom: 16px;
  color: #666;
}

.answer-page-question {
  margin-bottom: 16px;
  font-size: 18px;
}

.answer-page-actions {
  display: flex;
  justify-content: center;
}
</style>
