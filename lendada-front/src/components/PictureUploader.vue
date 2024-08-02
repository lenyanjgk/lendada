<template>
  <a-space :style="{ width: '100%' }" direction="vertical">
    <a-upload
      :custom-request="customRequest"
      :fileList="file ? [file] : []"
      :show-file-list="false"
    >
      <template #upload-button>
        <div
          :class="`arco-upload-list-item${
            file && file.status === 'error'
              ? ' arco-upload-list-item-error'
              : ''
          }`"
        >
          <div
            v-if="file && file.url"
            class="arco-upload-list-picture custom-upload-avatar"
          >
            <img :src="file.url" />
            <div class="arco-upload-list-picture-mask">
              <IconEdit />
            </div>
            <a-progress
              v-if="file.status === 'uploading' && file.percent < 100"
              :percent="file.percent"
              :style="{
                position: 'absolute',
                left: '50%',
                top: '50%',
                transform: 'translateX(-50%) translateY(-50%)',
              }"
              size="mini"
              type="circle"
            />
          </div>
          <div v-else class="arco-upload-picture-card">
            <div class="arco-upload-picture-card-text">
              <IconPlus />
              <div style="margin-top: 10px; font-weight: 600">上传</div>
            </div>
          </div>
        </div>
      </template>
    </a-upload>
  </a-space>
</template>

<script lang="ts" setup>
import { IconEdit, IconPlus } from "@arco-design/web-vue/es/icon";
import { defineProps, ref, withDefaults } from "vue";
import { uploadFileUsingPost } from "@/api/fileController";
import { Message } from "@arco-design/web-vue";

/**
 * 定义组件属性类型
 */
interface Props {
  biz: string;
  onChange?: (url: string) => void;
  value?: string;
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
});

const file = ref();
if (props.value) {
  file.value = {
    url: props.value,
    percent: 100,
    status: "done",
  };
}

// 自定义请求
const customRequest = async (option: any) => {
  const { onError, onSuccess, fileItem } = option;

  const res: any = await uploadFileUsingPost(
    { biz: props.biz },
    {},
    fileItem.file
  );
  if (res.data.code === 0 && res.data.data) {
    const url = res.data.data;
    file.value = {
      name: fileItem.name,
      file: fileItem.file,
      url,
    };
    props.onChange?.(url);
    onSuccess();
    console.log(file.value);
  } else {
    Message.error("上传失败，" + res.data.message || "");
    onError(new Error(res.data.message));
  }
};
</script>
