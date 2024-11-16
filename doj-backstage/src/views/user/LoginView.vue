<script setup lang="ts">
import {
  UserLoginDTO,
  UserControllerService,
  UserRegisterDTO,
} from "@/api/apiUser";
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";
const router = useRouter();
const store = useStore();
const onBack = () => {
  router.back();
};
const user = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginDTO);
const userRegister = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
} as UserRegisterDTO);
const radio = ref("User Login");
const Submit = async (type: string) => {
  if (type === "Login") {
    const res = await UserControllerService.loginUserUsingPost(user);
    if (res.code === 0) {
      await store.dispatch("user/getLoginUser");
      alert("登录成功");
      router.push("/");
    } else {
      alert("登陆失败");
    }
  } else {
    const res = await UserControllerService.userRegisterUsingPost(userRegister);
    if (res.code === 0) {
      await store.dispatch("user/userRegister");
      alert("注册成功");
      user.userAccount = userRegister.userAccount;
      user.userPassword = userRegister.userPassword;
      Submit("Login");
    } else {
      alert("注册失败");
    }
  }
};
</script>
<template>
  <div class="login-page">
    <el-card class="card">
      <template #header>
        <el-page-header @back="onBack">
          <template #content>
            <span>{{ radio }}</span>
          </template>
        </el-page-header>
      </template>
      <template v-if="radio === 'User Login'">
        <el-form class="form" label-position="top" :model="user">
          <el-form-item label="用户名:">
            <el-input v-model="user.userAccount" />
          </el-form-item>
          <el-form-item label="密码:">
            <el-input v-model="user.userPassword" type="password" />
          </el-form-item>
        </el-form>
      </template>
      <template v-else>
        <el-form class="form" label-position="top">
          <el-form-item label="用户名:">
            <el-input v-model="userRegister.userAccount" />
          </el-form-item>
          <el-form-item label="密码:">
            <el-input v-model="userRegister.userPassword" type="password" />
          </el-form-item>
          <el-form-item label="确认密码:">
            <el-input v-model="userRegister.checkPassword" type="password" />
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <div class="footer-content">
          <el-button
            type="primary"
            plain
            class="login_btn"
            @click="Submit(radio.substring(5))"
          >
            {{ radio.substring(5) }}
          </el-button>
          <el-radio-group v-model="radio" class="radio">
            <el-radio-button label="登录" value="User Login" />
            <el-radio-button label="注册" value="User Register" />
          </el-radio-group>
        </div>
      </template>
    </el-card>
  </div>
</template>
<style scoped>
.login-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
  background: rgba(255, 255, 255, 0.1);
  .card {
    width: 40%;
    span {
      font-size: xx-large;
      white-space: nowrap;
      overflow: hidden;
    }
    .form {
      max-width: 80%;
    }
  }
  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .login_btn {
      min-width: 100px;
      width: 40%;
    }
  }
}
</style>
