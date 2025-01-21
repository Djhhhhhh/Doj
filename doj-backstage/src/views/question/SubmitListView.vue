<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitQueryDTO,
} from "@/api/apiQuestion";
import { ElMessage } from "element-plus";
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
    const records = res.data.records;
    for (const record of records) {
      const questionRes =
        await QuestionControllerService.getQuestionByIdUsingGet(
          record.questionId
        );
      if (questionRes.code === 0) {
        record.questionTitle = questionRes.data.title;
      } else {
        record.questionTitle = "未知题目";
      }
    }
    dataList.value = records;
    total.value = res.data.total;
  } else {
    ElMessage.error("加载失败，" + res.message);
  }
};

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "评测编号",
    dataIndex: "id",
  },
  {
    title: "题目名称",
    slotName: "questionTitle",
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
      <el-form :model="searchParams" inline>
        <el-form-item label="题号" style="min-width: 240px">
          <el-input v-model="searchParams.questionId" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="编程语言" style="min-width: 240px">
          <el-select
            v-model="searchParams.language"
            style="width: 320px"
            placeholder="选择编程语言"
          >
            <el-option label="java" value="java" />
            <el-option label="cpp" value="cpp" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="doSubmit">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-table
        ref="tableRef"
        :data="dataList"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
        }"
        @current-change="onPageChange"
      >
        <el-table-column prop="id" label="评测编号" width="200" />
        <el-table-column label="题目名称">
          <template #default="{ row }">
            {{ row.questionTitle }}
          </template>
        </el-table-column>
        <el-table-column prop="language" label="编程语言" />
        <el-table-column label="判题状态">
          <template #default="{ row }">
            <el-tag
              :style="{
                background:
                  row.judgeInfo.message === 'Accepted'
                    ? 'green'
                    : row.judgeInfo.message === 'Time limit exceeded'
                    ? 'blue'
                    : row.judgeInfo.message === 'Wrong Answer'
                    ? 'red'
                    : 'black',
                color: 'white',
              }"
            >
              {{
                row.judgeInfo.message === null ||
                row.judgeInfo.message === undefined
                  ? "System Error"
                  : row.judgeInfo.message
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="运行时间">
          <template #default="{ row }"> {{ row.judgeInfo.time }}ms </template>
        </el-table-column>
        <el-table-column label="运行内存">
          <template #default="{ row }"> {{ row.judgeInfo.memory }} </template>
        </el-table-column>
        <el-table-column prop="status" label="判题状态" />
        <el-table-column label="创建时间">
          <template #default="{ row }">
            {{ moment(row.createTime).format("YYYY-MM-DD") }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-space>
              <el-button
                type="primary"
                plain
                @click="routeGo('/question/submit/list/information', row.id)"
              >
                详细
              </el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
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
