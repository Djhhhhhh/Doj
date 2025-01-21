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
    // 获取题目数据
    dataList.value = res.data.records;

    // 获取每个题目的标签和标签名称
    for (const item of dataList.value) {
      item.updateTime = formatDate(item.updateTime);

      // 获取标签 ID 列表
      const tagRes =
        await QuestionControllerService.getQuestionTagByQuetsionIdUsingPost(
          item.id
        ); // 获取与题目 ID 关联的标签 ID 列表

      console.log(tagRes);
      if (tagRes.code === 0) {
        // 假设返回的数据中包含 tagId，将其保持为字符串类型
        const tagIds = tagRes.data.map((tag: { tagId: string }) => tag.tagId); // 保留 tagId 为字符串类型

        // 根据标签 ID 获取标签名称
        const tagNames = await Promise.all(
          tagIds.map(async (tagId: string) => {
            // tagId 为字符串类型
            const tagNameRes =
              await QuestionControllerService.getTagByIdUsingGet(tagId); // 传递字符串类型的 tagId
            return tagNameRes.code === 0 ? tagNameRes.data.name : "未知标签";
          })
        );

        item.tagsName = tagNames; // 保存标签名称
      } else {
        item.tagsName = ["未知标签"]; // 如果没有标签，则返回默认值
      }
    }
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
              </template>
              <div style="white-space: pre-wrap; word-break: break-word">
                {{
                  item.content.split("\n").length > 2
                    ? item.content.split("\n").slice(0, 2).join(" ") +
                      "(点击查看全部)"
                    : item.content
                }}
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
              <div>
                <!-- 渲染标签 -->
                <el-tag
                  v-for="(tag, index) in item.tagsName"
                  :key="index"
                  style="margin: 5px"
                >
                  {{ tag }}
                </el-tag>
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
