<script setup lang="ts">
import { onMounted, onUnmounted, reactive, computed, ref } from "vue";
import moment from "moment";
import { UserAttendanceVO, UserControllerService } from "@/api/apiUser";

const form = reactive({
  userId: 0,
  checkInTime: "",
  checkInStatus: false,
}) as UserAttendanceVO;

const checkIn = async () => {
  await UserControllerService.checkInUsingGet();
  loadData();
};

const checkOut = async () => {
  await UserControllerService.checkOutUsingGet();
  loadData();
};

const dataList = ref<UserAttendanceVO[]>([]);

const loadData = async () => {
  const res = await UserControllerService.getUserAttendanceStatusUsingGet();
  if (res.code === 0) {
    form.userId = res.data?.userId;
    form.checkInTime = res.data?.checkInTime;
    form.checkInStatus = res.data?.checkInStatus;
  }
  const res2 =
    await UserControllerService.getUserAttendanceStatusListUsingGet();
  if (res2.code === 0) {
    dataList.value = res2.data;
    dataList.value.forEach(async (user) => {
      if (user.checkInTime && user.checkInStatus) {
        const checkInMoment = moment(user.checkInTime);
        const duration = moment().diff(checkInMoment, "minutes");
        user.learningTime = duration;
      } else {
        user.learningTime = 0; // 如果没有签到时间，学习时间为0
      }
      const res3 = await UserControllerService.getUserByIdUsingGet(user.userId);
      if (res3.code === 0) {
        user.userName = res3.data.userName || "未知用户";
      }
    });
    dataList.value.sort((a, b) => b.learningTime - a.learningTime);
    dataList.value.forEach((user, index) => {
      user.rank = index + 1; // 排名从 1 开始
    });
    console.log(dataList);
  }
};

const learningMinutes = computed(() => {
  if (form.checkInTime && form.checkInStatus) {
    const checkInMoment = moment(form.checkInTime);
    const duration = moment().diff(checkInMoment, "minutes");
    return duration;
  }
  return 0;
});

let timer: ReturnType<typeof setInterval> | null = null;

onMounted(() => {
  loadData();
  timer = setInterval(() => {
    loadData();
  }, 60000);
});

onUnmounted(() => {
  if (timer) {
    clearInterval(timer);
  }
});
const columns = [
  { label: "排名", prop: "rank" },
  { label: "用户名", prop: "userName" },
  {
    label: "签到时间",
    prop: "checkInTime",
    render: (row: UserAttendanceVO) =>
      moment(row.checkInTime).format("YYYY/MM/DD HH:mm:ss"),
  },
  {
    label: "学习时间（分钟）",
    prop: "learningTime",
    render: (row: UserAttendanceVO) => row.learningTime,
  },
];
</script>

<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card>
        <div class="card-header" v-if="form.checkInStatus === false">
          <span>若在502集训室，请签到</span>
          <el-button type="success" plain @click="checkIn()">
            点击签到
          </el-button>
        </div>
        <div class="card-header" v-else>
          <span>欢迎进入502集训室</span>
          <el-button type="danger" plain @click="checkOut()">
            点击签退
          </el-button>
        </div>
        <div v-if="form.checkInStatus === true">
          进入时间：{{ moment(form.checkInTime).format("YYYY/MM/DD HH:mm:ss") }}
          <br />
          您已学习：{{ learningMinutes }} 分钟
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="10">
      <el-card class="rank-card">
        <template #header>
          <span class="rank-title">502内卷排行榜</span>
        </template>
        <el-table :data="dataList" stripe>
          <el-table-column
            v-for="column in columns"
            :key="column.prop"
            :label="column.label"
            :prop="column.prop"
            :formatter="column.render"
            :width="column.prop === 'rank' ? '60px' : ''"
          />
        </el-table>
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  font-size: large;
}
.rank-card {
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  margin-top: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  max-height: calc(100vh - 200px);
}

.rank-title {
  font-size: x-large;
  text-align: center;
  width: 100%;
  display: block;
}

.el-table {
  border-radius: 8px;
}

.el-table th {
  background-color: #f0f0f0;
  font-weight: bold;
}

.el-table-column {
  padding: 10px;
}

.el-table tr:hover {
  background-color: #f5f5f5;
}

.el-button {
  margin: 10px 0;
}

.el-table td {
  text-align: center;
}

.el-card {
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.el-table th,
.el-table td {
  padding: 10px;
  text-align: center;
}
</style>
