<template>
  <a-modal
    v-model:visible="visible"
    :footer="false"
    transition-name="zoom-in-top"
    @cancel="closeModal"
  >
    <template #title>
      <div class="modal-title">{{ title }}</div>
    </template>
    <div class="modal-content">
      <h4>复制分享链接</h4>
      <a-typography-paragraph class="link-paragraph" copyable
        >{{ link }}
      </a-typography-paragraph>
      <h4>手机扫码查看</h4>
      <div class="qr-code-container">
        <img :src="codeImg" alt="QR Code" />
      </div>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { defineExpose, defineProps, ref, withDefaults } from "vue";
import QRCode from "qrcode";
import message from "@arco-design/web-vue/es/message";

/**
 * 定义组件属性类型
 */
interface Props {
  // 分享链接
  link: string;
  // 弹窗标题
  title: string;
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  link: () => "https://laoyujianli.com/share/yupi",
  title: () => "分享",
});

// 要展示的图片
const codeImg = ref();

// 是否可见
const visible = ref(false);

// 打开弹窗
const openModal = () => {
  visible.value = true;
};

// 暴露函数给父组件
defineExpose({
  openModal,
});

// 关闭弹窗
const closeModal = () => {
  visible.value = false;
};

// 二维码生成
QRCode.toDataURL(props.link)
  .then((url) => {
    codeImg.value = url;
  })
  .catch((err) => {
    console.error(err);
    message.error("生成二维码失败，" + err.message);
  });
</script>

<style scoped>
.modal-title {
  color: #165dff;
  font-weight: bold;
  font-size: 1.2em;
}

.modal-content {
  padding: 16px;
}

.link-paragraph {
  margin-bottom: 16px;
  color: #333;
  font-size: 14px;
}

.qr-code-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 50px;
  border: 1px solid #ada6f6;
  border-radius: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>
