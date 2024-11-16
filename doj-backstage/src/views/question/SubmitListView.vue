<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitQueryDTO,
} from "@/api/apiQuestion";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();
const routeGo = (url: string, i: string) => {
  router.push({
    path: url,
    query: {
      id: i,
    },
  });
};
const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryDTO>({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    {
      ...searchParams.value,
      sortField: "createTime",
      sortOrder: "descend",
    }
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败，" + res.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "提交者 id",
    dataIndex: "userId",
  },
  {
    title: "题目 id",
    dataIndex: "questionId",
  },
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "判题状态",
    slotName: "judgeInfo",
  },
  {
    title: "运行时间",
    slotName: "time",
  },
  {
    title: "运行内存",
    slotName: "memory",
  },
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

/**
 * 确认搜索，重新加载数据
 */
const doSubmit = () => {
  // 这里需要重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>

<template>
  <el-card style="height: 95%">
    <div id="questionSubmitView">
      <a-form :model="searchParams" layout="inline">
        <a-form-item field="questionId" label="题号" style="min-width: 240px">
          <a-input v-model="searchParams.questionId" placeholder="请输入" />
        </a-form-item>
        <a-form-item field="language" label="编程语言" style="min-width: 240px">
          <a-select
            v-model="searchParams.language"
            :style="{ width: '320px' }"
            placeholder="选择编程语言"
          >
            <a-option>java</a-option>
            <a-option>cpp</a-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="doSubmit">搜索</a-button>
        </a-form-item>
      </a-form>
      <a-divider size="0" />
      <a-table
        :ref="tableRef"
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
        <template #judgeInfo="{ record }">
          <a-tag
            :class="{
              'green-tag': record.judgeInfo.message === 'Accepted',
              'red-tag': record.judgeInfo.message === 'Wrong Answer',
              'blue-tag': record.judgeInfo.message === 'Time limit exceeded',
            }"
          >
            {{
              record.judgeInfo.message === null ||
              record.judgeInfo.message === undefined
                ? "System Error"
                : record.judgeInfo.message
            }}
          </a-tag>
        </template>
        <template #time="{ record }"> {{ record.judgeInfo.time }}ms </template>
        <template #memory="{ record }">
          {{ record.judgeInfo.memory }}
        </template>
        <template #createTime="{ record }">
          {{ moment(record.createTime).format("YYYY-MM-DD") }}
        </template>
        <template #optional="{ record }">
          <a-space>
            <el-button
              type="primary"
              plain
              @click="routeGo('/question/submit/list/information', record.id)"
            >
              详细
            </el-button>
          </a-space>
        </template>
      </a-table>
    </div>
  </el-card>
  <router-view />
</template>

<style scoped>
.green-tag {
  background-color: green;
  color: white;
}

.red-tag {
  background-color: red;
  color: white;
}

.blue-tag {
  background-color: blue;
  color: white;
}
</style>
