<template>
  <a-form
    :model="formSearchParams"
    :style="{ marginBottom: '20px' }"
    @submit="doSearch"
  >
    <a-row :gutter="16">
      <a-col :span="8">
        <a-form-item field="id" label="用户ID">
          <a-input
            v-model="formSearchParams.id"
            :max-length="20"
            allow-clear
            placeholder="请输入用户id"
            show-word-limit
          />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item field="userName" label="用户名">
          <a-input
            v-model="formSearchParams.userName"
            :max-length="10"
            allow-clear
            placeholder="请输入用户名"
            show-word-limit
          />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :span="8">
        <a-form-item field="userProfile" label="用户简介">
          <a-input
            v-model="formSearchParams.userProfile"
            allow-clear
            placeholder="请输入用户简介"
          />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item field="userRole" label="用户角色">
          <a-input
            v-model="formSearchParams.userRole"
            :max-length="10"
            allow-clear
            placeholder="请输入用户角色"
            show-word-limit
          />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-button html-type="submit" style="width: 100px" type="primary">
          搜 索
        </a-button>
      </a-col>
    </a-row>
  </a-form>
  <a-table
    :columns="columns"
    :data="dataList"
    :pagination="{
      showTotal: true,
      pageSize: searchParams.pageSize,
      current: searchParams.current,
      total,
    }"
    @page-change="onPageChange"
  >
    <template #userAvatar="{ record }">
      <a-image :src="record.userAvatar" width="64" />
    </template>
    <template #createTime="{ record }">
      {{ dayjs(record.createTime).format("YYYY-MM-DD") }}
    </template>
    <template #updateTime="{ record }">
      {{ dayjs(record.updateTime).format("YYYY-MM-DD") }}
    </template>
    <template #optional="{ record }">
      <a-space>
        <a-button status="danger" @click="doDelete(record)">删除</a-button>
      </a-space>
    </template>
  </a-table>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import {
  deleteUserUsingPost,
  listUserByPageUsingPost,
} from "@/api/userController";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";

const formSearchParams = ref<API.UserQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.UserQueryRequest>({
  ...initSearchParams,
});
const dataList = ref<API.User[]>([]);
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listUserByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 执行搜索
 */
const doSearch = () => {
  searchParams.value = {
    ...initSearchParams,
    ...formSearchParams.value,
  };
};

/**
 * 当分页变化时，改变搜索条件，触发数据加载
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 删除
 * @param record
 */
const doDelete = async (record: API.User) => {
  if (!record.id) {
    return;
  }

  const res = await deleteUserUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

// 表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "用户名",
    dataIndex: "userName",
  },
  {
    title: "用户头像",
    dataIndex: "userAvatar",
    slotName: "userAvatar",
  },
  {
    title: "用户简介",
    dataIndex: "userProfile",
  },
  {
    title: "权限",
    dataIndex: "userRole",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>
