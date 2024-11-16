<script lang="ts" setup>
import { onMounted, ref, toRaw } from "vue";
import { QuestionControllerService } from "@/api/apiQuestion";
import { Edit } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
const router = useRouter();
const dataList = ref([]);
const searchParams = ref({
  pageSize: 10,
  pageNum: 1,
});
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "用户id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return new Intl.DateTimeFormat("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  }).format(date);
};
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    dataList.value = res.data.records.map((item) => ({
      ...item,
      tags: JSON.parse(item.tags),
      updateTime: formatDate(item.updateTime),
    }));
  } else {
    alert("加载失败，" + res.message);
  }
};
onMounted(() => {
  loadData();
  console.log(dataList);
});
const routeGo = (url: string, i: number) => {
  if (i === -1) {
    router.push(url);
  } else {
    router.push({
      path: url,
      query: {
        id: i,
      },
    });
  }
};
</script>

<template>
  <div class="card-container">
    <el-container>
      <el-aside class="question_list">
        <el-card>
          <template #header>
            <div class="beteen_box">
              <span class="title">题目列表</span>
              <el-button
                type="primary"
                @click="routeGo('/question/add', -1)"
                plain
              >
                添加题目
              </el-button>
            </div>
          </template>
          <el-collapse accordion>
            <el-collapse-item
              v-for="(item, index) in dataList"
              :key="index"
              :name="index"
            >
              <template #title>
                <el-button
                  size="small"
                  :icon="Edit"
                  circle
                  @click="routeGo('/question/update', item.id)"
                />
                <div style="margin-right: 10px; margin-left: 10px">
                  {{ item.title }}
                </div>
                <el-tag
                  v-for="(tag, id) in item.tags"
                  :key="id"
                  type="primary"
                  style="margin-right: 3px"
                >
                  {{ tag }}
                </el-tag>
              </template>
              <div>
                {{ item.content }}
              </div>
              <div>
                <el-text
                  class="mx-1"
                  type="info"
                  style="display: block; text-align: end"
                >
                  {{ item.updateTime }}
                </el-text>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-aside>
      <el-container>
        <el-main>
          <el-card
            v-if="toRaw(router).currentRoute.value.fullPath === '/question'"
          >
            <h1>公告板</h1>
          </el-card>
          <el-card v-else style="height: 80vh; overflow: auto">
            <router-view />
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.title {
  font-size: large;
}
.question_list {
  width: 40%;
  height: auto;
  max-height: 100%;
  white-space: nowrap;
  .beteen_box {
    display: flex;
    justify-content: space-between;
  }
}
</style>
