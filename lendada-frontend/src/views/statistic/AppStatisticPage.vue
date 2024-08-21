<template>
  <div id="appStatisticPage">
    <div class="search-bar">
      <a-input-search
        :style="{ width: '320px' }"
        button-text="搜索"
        placeholder="输入 appId"
        search-button
        size="large"
        style="margin-bottom: 10px"
        @search="(value) => loadAppAnswerResultCountData(value)"
      />
    </div>

    <a-row gutter="16">
      <a-col span="12">
        <a-card
          ><h2>热门应用统计</h2>
          <v-chart :option="appAnswerCountOptions" style="height: 300px" />
        </a-card>
      </a-col>

      <a-col span="12">
        <a-card>
          <h2>应用结果统计</h2>
          <div style="margin-bottom: 16px" />
          <v-chart
            :option="appAnswerResultCountOptions"
            style="height: 300px"
          />
        </a-card>
      </a-col>
    </a-row>

    <a-row gutter="16" style="margin-top: 20px">
      <a-col span="24">
        <a-card
          ><h2>热门应用计量</h2>
          <v-chart :option="appOptionCountOptions" style="height: 300px" />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watchEffect } from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import {
  getAppAnswerCountUsingGet,
  getAppAnswerResultCountUsingGet,
} from "@/api/appStatisticController";
import "echarts";
import VChart from "vue-echarts";

const appAnswerCountList = ref<API.AppAnswerCountDTO[]>([]);
const appAnswerResultCountList = ref<API.AppAnswerResultCountDTO[]>([]);

/**
 * 加载热门应用答案计数数据
 */
const loadAppAnswerCountData = async () => {
  const res = await getAppAnswerCountUsingGet();
  if (res.data.code === 0) {
    appAnswerCountList.value = res.data.data || [];
  } else {
    message.error("获取热门应用答案计数数据失败，" + res.data.message);
  }
};

/**
 * 加载指定应用的答案结果计数数据
 * @param appId
 */
const loadAppAnswerResultCountData = async (appId: string) => {
  if (!appId) {
    return;
  }
  const res = await getAppAnswerResultCountUsingGet({
    appId: appId as any,
  });
  if (res.data.code === 0) {
    appAnswerResultCountList.value = res.data.data || [];
  } else {
    message.error("获取指定应用的答案结果计数数据失败，" + res.data.message);
  }
};

// 热门应用答案计数图表选项
const appAnswerCountOptions = computed(() => {
  return {
    xAxis: {
      type: "category",
      data: appAnswerCountList.value.map((item) => item.appId),
      name: "应用 id",
    },
    yAxis: {
      type: "value",
      name: "用户答案数",
    },
    series: [
      {
        data: appAnswerCountList.value.map((item) => item.answerCount),
        type: "bar",
      },
    ],
  };
});

// 应用答案结果分布图表选项
const appAnswerResultCountOptions = computed(() => {
  return {
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "应用答案结果分布",
        type: "pie",
        radius: "50%",
        data: appAnswerResultCountList.value.map((item) => {
          return { value: item.resultCount, name: item.resultName };
        }),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  };
});

const appOptionCountOptions = computed(() => {
  return {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        crossStyle: {
          color: "#999",
        },
      },
    },
    toolbox: {
      feature: {
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ["line", "bar"] },
        restore: { show: true },
        saveAsImage: { show: true },
      },
    },
    legend: {
      data: ["答案次数", "回答次数", "趋势"],
    },
    xAxis: [
      {
        type: "category",
        data: appAnswerCountList.value.map((item) => item.appId),
        axisPointer: {
          type: "shadow",
        },
      },
    ],
    yAxis: [
      {
        type: "value",
        name: "回答次数",
        min: 0,
        max: 20,
        interval: 5,
        axisLabel: {
          formatter: "{value} 次",
        },
      },
      {
        type: "value",
        name: "趋势",
        min: 0,
        max: 100,
        interval: 20,
        axisLabel: {
          formatter: "{value} ",
        },
      },
    ],
    series: [
      {
        name: "答案次数",
        type: "bar",
        tooltip: {
          valueFormatter: function (value) {
            return value + " 次";
          },
        },
        data: appAnswerCountList.value.map((item) => item.answerCount),
      },
      {
        name: "回答次数",
        type: "bar",
        tooltip: {
          valueFormatter: function (value) {
            return value + " 次";
          },
        },
        data: appAnswerCountList.value.map((item) => item.answerCount),
      },
      {
        name: "趋势",
        type: "line",
        yAxisIndex: 1,
        tooltip: {
          valueFormatter: function (value) {
            return value + " %";
          },
        },
        data: appAnswerCountList.value.map((item) => item.answerCount + 1),
      },
    ],
  };
});
/**
 * 当组件挂载时加载热门应用答案计数数据
 */
watchEffect(() => {
  loadAppAnswerCountData();
});

/**
 * 当搜索参数改变时加载应用答案结果计数数据
 */
watchEffect(() => {
  loadAppAnswerResultCountData("");
});
</script>

<style scoped></style>
