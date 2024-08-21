<template>
  <div id="userRegisterPage" class="login-page">
    <h2>用户注册</h2>
    <a-form
      :model="form"
      :style="{ maxWidth: '550px', margin: '0 auto' }"
      auto-label-width
      label-align="left"
      @submit="handleSubmit"
    >
      <div class="input-group">
        <icon-user-add class="icon" size="20px" />
        <a-form-item field="userAccount" label="账号">
          <a-input
            v-model="form.userAccount"
            class="input-field"
            placeholder="请输入账号"
          />
        </a-form-item>
      </div>
      <div class="input-group">
        <icon-code class="icon" size="20px" />
        <a-form-item
          field="userPassword"
          label="密码"
          tooltip="密码不小于 8 位"
        >
          <a-input-password
            v-model="form.userPassword"
            class="input-field"
            placeholder="请输入密码"
          />
        </a-form-item>
      </div>
      <div class="input-group">
        <icon-code class="icon" size="20px" />
        <a-form-item
          field="checkPassword"
          label="确认密码"
          tooltip="确认密码不小于 8 位"
        >
          <a-input-password
            v-model="form.checkPassword"
            class="input-field"
            placeholder="请输入确认密码"
          />
        </a-form-item>
      </div>

      <a-form-item>
        <div class="actions">
          <a-button class="submit-button" html-type="submit" type="primary">
            注册
          </a-button>
          <a-button class="register-link" href="/user/login" type="outline">
            老用户登录
          </a-button>
        </div>
      </a-form-item>
      <div class="other-login-methods">
        <span
          >或使用以下方注册: （暂未实现）
          <div class="social-logins">
            <a-button shape="circle" type="outline">
              <icon-qq />
            </a-button>
            <a-button shape="circle" type="outline">
              <icon-wechat />
            </a-button>
            <a-button shape="circle" type="outline">
              <icon-lark-color />
            </a-button>
            <a-button shape="circle" type="outline">
              <icon-github />
            </a-button>
          </div>
        </span>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import API from "@/api";
import { userRegisterUsingPost } from "@/api/userController";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
} as API.UserRegisterRequest);

/**
 * 提交
 */
const handleSubmit = async () => {
  const res = await userRegisterUsingPost(form);
  if (res.data.code === 0) {
    message.success("注册成功");
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注册失败，" + res.data.message);
  }
};
</script>
<style scoped>
/* 复制登录页面的样式 */
.login-page {
  width: 500px;
  margin: auto;
  margin-top: 100px;
  padding: 30px;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.login-page h2 {
  margin-bottom: 30px;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.icon {
  margin-right: 10px;
  margin-bottom: 20px;
  color: #999;
}

.input-field {
  width: 100%;
  max-width: 400px;
}

.actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: auto;
}

.submit-button {
  width: 100px;
  margin-right: 80px;
}

.register-link {
}

.other-login-methods {
  margin-top: 20px;
  text-align: center;
}

.social-logins {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 10px;
}

.social-logins a-button {
  width: 100px;
}
</style>
