<script setup lang="ts">
import store from "@/store";
import { onMounted, reactive } from "vue";
import { UserUpdateDTO, UserControllerService } from "@/api/apiUser";
import moment from "moment";
const from = reactive({
  id: 0,
  userAvatar: "",
  userName: "",
  userProfile: "",
  userRole: "",
}) as UserUpdateDTO;
const mapValues = () => {
  from.id = store.state.user.loginUser.id;
  from.userAvatar = store.state.user.loginUser.userAvatar;
  from.userName = store.state.user.loginUser.userName;
  from.userProfile = store.state.user.loginUser.userProfile;
  from.userRole = store.state.user.loginUser.userRole;
};
const update = async () => {
  const res = await UserControllerService.updateUserUsingPost(from);
  await store.dispatch("user/getLoginUser");
  alert(res.message);
};
onMounted(() => {
  mapValues();
  console.log(from.userRole);
});
</script>
<template>
  <el-card>
    <el-button plain @click="update()">修改信息</el-button>
    <el-descriptions direction="vertical" border style="margin-top: 5px">
      <el-descriptions-item
        :rowspan="2"
        :width="140"
        label="Photo"
        align="center"
      >
        <el-image
          style="width: 100px; height: 100px"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        />
      </el-descriptions-item>
      <el-descriptions-item label="用户编号">
        {{ from.id }}
      </el-descriptions-item>
      <el-descriptions-item label="权限">
        <el-tag size="small">{{ from.userRole }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="用户昵称">
        <el-input v-model="from.userName" style="width: 60%" />
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ moment(store.state.user.loginUser.createTime).format("YYYY/MM/DD") }}
      </el-descriptions-item>
      <el-descriptions-item label="Address"> 个人介绍 </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>
<style scoped></style>
