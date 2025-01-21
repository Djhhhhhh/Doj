<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryDTO,
} from "@/api/apiQuestion";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();

const tableRef = ref();

const dataList = ref<Question[]>([]);
const total = ref(0);
const searchParams = ref<QuestionQueryDTO>({
  title: "",
  pageSize: 8,
  current: 1,
});

const selectedTags = ref<number[]>([]);
const allTags = ref([]);

const loadTags = async () => {
  const res = await QuestionControllerService.listTagsUsingPost();
  if (res.code === 0) {
    allTags.value = res.data;
  } else {
    ElMessage.error("加载标签失败，" + res.message);
  }
};

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    const questions = res.data.records;
    for (const question of questions) {
      const tagsRes =
        await QuestionControllerService.getQuestionTagByQuetsionIdUsingPost(
          question.id
        );
      if (tagsRes.code === 0) {
        const tagNames = [];
        for (const tag of tagsRes.data) {
          const tagRes = await QuestionControllerService.getTagByIdUsingGet(
            tag.tagId
          );
          if (tagRes.code === 0) {
            tagNames.push(tagRes.data.name);
          }
        }
        question.tags = tagNames;
      } else {
        question.tags = [];
      }
    }
    // 根据选中的标签名称进行过滤
    if (selectedTags.value.length > 0) {
      dataList.value = questions.filter((question) =>
        selectedTags.value.every((tagId) =>
          question.tags.includes(
            allTags.value.find((tag) => tag.id === tagId)?.name
          )
        )
      );
    } else {
      dataList.value = questions;
    }
    total.value = res.data.total;
  } else {
    ElMessage.error("加载数据失败，" + res.message);
  }
};

const onSearch = () => {
  searchParams.value.current = 1;
  loadData();
};

const onReset = () => {
  searchParams.value.title = "";
  selectedTags.value = [];
  onSearch();
};

const onPageChange = (page: number) => {
  searchParams.value.current = page;
  loadData();
};

const toQuestionPage = (record: Question) => {
  router.push(`/question/submit?id=${record.id}`);
};

onMounted(() => {
  loadTags();
  loadData();
});
</script>
<template>
  <el-card>
    <el-form :model="searchParams" @submit.prevent="onSearch" inline>
      <el-form-item label="题目名称">
        <el-input v-model="searchParams.title" placeholder="请输入题目名称" />
      </el-form-item>
      <el-form-item label="标签">
        <el-select
          v-model="selectedTags"
          multiple
          placeholder="请选择标签"
          style="width: 200px"
        >
          <el-option
            v-for="tag in allTags"
            :key="tag.id"
            :label="tag.name"
            :value="tag.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <el-table ref="tableRef" :data="dataList">
      <el-table-column prop="id" label="题目编号" />
      <el-table-column prop="title" label="题目名称" />
      <el-table-column prop="tags" label="标签">
        <template #default="{ row }">
          <el-space wrap>
            <el-tag
              v-for="(tag, index) of row.tags"
              :key="index"
              type="success"
            >
              {{ tag }}
            </el-tag>
          </el-space>
        </template>
      </el-table-column>
      <el-table-column prop="acceptedRate" label="通过率">
        <template #default="{ row }">
          {{
            `${
              row.submitNum
                ? ((row.acNum / row.submitNum) * 100).toFixed(2)
                : "0.00"
            }% (${row.acNum}/${row.submitNum})`
          }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间">
        <template #default="{ row }">
          {{ moment(row.createTime).format("YYYY-MM-DD") }}
        </template>
      </el-table-column>
      <el-table-column prop="optional" label="操作">
        <template #default="{ row }">
          <el-space>
            <el-button type="primary" plain @click="toQuestionPage(row)">
              做题
            </el-button>
          </el-space>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination" style="text-align: end; margin-top: 20px">
      <el-button
        :disabled="searchParams.current === 1"
        @click="onPageChange(searchParams.current - 1)"
        plain
      >
        上一页
      </el-button>
      <span class="page-info" style="margin: 0 10px">
        {{ searchParams.current }} /
        {{ Math.ceil(total / searchParams.pageSize) }}
      </span>
      <el-button
        :disabled="
          searchParams.current === Math.ceil(total / searchParams.pageSize)
        "
        @click="onPageChange(searchParams.current + 1)"
        plain
      >
        下一页
      </el-button>
    </div>
  </el-card>
</template>

<style scoped>
.form {
  max-width: 800px;
  margin: 0 auto;
}

.full-width {
  width: 100%;
}

.delete-button {
  text-align: right;
}

.delete-btn {
  margin-top: 10px;
}

.add-button {
  margin-top: 32px;
  text-align: center;
}

.submit-button {
  margin-top: 16px;
  text-align: center;
}
</style>
