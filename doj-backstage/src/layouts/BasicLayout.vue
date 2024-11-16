<script setup lang="ts">
import { ref } from "vue";
import {
  Menu as IconMenu,
  Histogram,
  Operation,
  Refresh,
  Avatar,
  Grid,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { UserControllerService } from "@/api/apiUser";
const store = useStore();
console.log(store);
const router = useRouter();
const isCollapse = ref(false);
const isCollapseText = ref("收起侧边栏");
const collapse = () => {
  isCollapse.value = !isCollapse.value;
  if (isCollapseText.value === "收起侧边栏") {
    isCollapseText.value = "展开侧边栏";
  } else {
    isCollapseText.value = "收起侧边栏";
  }
};
const optionsVisible = ref(false);
const showOptions = () => {
  optionsVisible.value = true;
};
const hideOptions = () => {
  optionsVisible.value = false;
};
const routeGo = (url: string) => {
  router.push(url);
};
const logOut = async () => {
  const res = await UserControllerService.userLogoutUsingPost();
  if (res.code === 0) {
    alert("登出成功");
    window.location.reload();
  }
};
</script>
<template>
  <el-container class="common-layout">
    <el-menu :default-active="1" :collapse="isCollapse" class="aside">
      <el-carousel
        height="200px"
        direction="vertical"
        type="card"
        :autoplay="false"
        class="carousel"
      >
        <el-carousel-item v-for="item in 4" :key="item">
          <h3 text="2xl" justify="center">{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>
      <el-menu-item index="1" @click="routeGo('/')">
        <el-icon><Histogram /></el-icon>
        <template #title>数据面板</template>
      </el-menu-item>
      <el-menu-item
        index="2"
        @click="routeGo('/question')"
        v-if="store.state.user.loginUser.userRole === 'admin'"
      >
        <el-icon><Operation /></el-icon>
        <template #title>管理题目</template>
      </el-menu-item>
      <el-menu-item index="3" @click="routeGo('/question/list')">
        <el-icon><icon-menu /></el-icon>
        <template #title>题目列表</template>
      </el-menu-item>
      <el-menu-item index="4" @click="routeGo('/question/submit/list')">
        <el-icon><Grid /></el-icon>
        <template #title>题目提交列表</template>
      </el-menu-item>
      <el-menu-item index="5" @click="routeGo('/user/updata')">
        <el-icon><Avatar /></el-icon>
        <template #title>个人信息</template>
      </el-menu-item>
      <el-menu-item @click="collapse()">
        <el-icon><Refresh /></el-icon>
        <template #title>{{ isCollapseText }}</template>
      </el-menu-item>
    </el-menu>
    <el-container>
      <el-header class="head">
        <el-link :underline="false" href="/" class="link">Doj</el-link>
        <div class="avatar" @mouseover="showOptions" @mouseleave="hideOptions">
          <el-dropdown>
            <el-avatar :size="60">
              {{ store.state.user.loginUser.userName }}
            </el-avatar>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="routeGo('/user/login')">
                  登录
                </el-dropdown-item>
                <el-dropdown-item @click="routeGo('/user/updata')">
                  个人设置
                </el-dropdown-item>
                <el-dropdown-item divided @click="logOut()">
                  登出
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<style scoped>
.common-layout {
  height: 100%;
  .aside {
  }
  .head {
    height: 10%;
    background-color: blanchedalmond;
    display: flex;
    justify-content: space-between;
    .link {
      align-items: flex-end;
      font-size: xx-large;
    }
    .avatar {
      position: relative;
      display: flex;
      align-items: center;
      margin-right: 2%;
    }
  }
  .aside {
    el-menu-item {
      margin-top: 20px;
    }
    .el-carousel__item h3 {
      color: #475669;
      opacity: 0.75;
      line-height: 100px;
      margin: 0;
      text-align: center;
    }
    .el-carousel__item:nth-child(2n) {
      background-color: #99a9bf;
    }
    .el-carousel__item:nth-child(2n + 1) {
      background-color: #d3dce6;
    }
    .carousel {
      margin-bottom: 20%;
      margin-top: 10%;
      margin-left: 10px;
      margin-right: 10px;
    }
  }
}
</style>
