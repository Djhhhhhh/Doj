<script setup lang="ts">
import { onMounted, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { QuestionControllerService } from "@/api/apiQuestion";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
// 如果页面地址包含 update，视为更新页面
const updatePage = route.path.includes("update");

let form = ref({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});

const allTags = ref([]);

// 获取标签名称列表
const loadTagNames = async () => {
  const tagIds = form.value.tags; // 确保form.value.tags是标签ID列表
  const tagNames = await Promise.all(
    tagIds.map(async (tagId: string) => {
      const tagNameRes = await QuestionControllerService.getTagByIdUsingGet(
        tagId
      ); // 传递字符串类型的 tagId
      return tagNameRes.code === 0 ? tagNameRes.data.name : "未知标签";
    })
  );
  form.value.tags = tagNames; // 更新为标签名称
};

const loadAllTags = async () => {
  const tagRes = await QuestionControllerService.listTagsUsingPost();
  if (tagRes.code === 0) {
    allTags.value = tagRes.data;
  }
};

onMounted(() => {
  loadAllTags();
});

const doSubmit = async () => {
  try {
    const questionRequest = {
      ...form.value,
      tags: [], // 清空 tags，避免重复保存
    };

    let res;
    if (updatePage) {
      res = await QuestionControllerService.updateQuestionUsingPost(
        questionRequest
      );
    } else {
      res = await QuestionControllerService.addQuestionUsingPost(
        questionRequest
      );
    }

    if (res.code === 0) {
      const questionId = res.data;
      await saveTags(questionId);
      ElMessage.success(updatePage ? "更新成功" : "创建成功");
      router.push("/question/list");
    } else {
      ElMessage.error((updatePage ? "更新" : "创建") + "失败，" + res.message);
    }
  } catch (error) {
    ElMessage.error("提交失败，" + error.message);
  }
};

const saveTags = async (questionId) => {
  const tags = form.value.tags;
  for (const tagId of tags) {
    const questionTag = {
      questionId,
      tagId,
    };
    await QuestionControllerService.addQuestionTagUsingPost(questionTag);
  }
};

/**
 * 新增判题用例
 */
const handleAdd = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};

/**
 * 删除判题用例
 */
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.value.content = value;
};

const onAnswerChange = (value: string) => {
  form.value.answer = value;
};
const onBack = () => {
  router.push("/question");
};
</script>

<template>
  <div id="addQuestionView" class="container">
    <el-page-header @back="onBack"></el-page-header>
    <h2>添加题目</h2>
    <el-card>
      <el-form :model="form" ref="formRef" label-position="left" class="form">
        <el-form-item prop="title" label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item prop="tags" label="标签">
          <el-select v-model="form.tags" placeholder="请选择标签" multiple>
            <el-option
              v-for="tag in allTags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="题目内容" class="full-width">
          <MdEditor
            :value="form.content"
            :handle-change="onContentChange"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="answer" label="答案模板" class="full-width">
          <MdEditor
            :value="form.answer"
            :handle-change="onAnswerChange"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="判题配置">
          <el-form-item
            prop="judgeConfig.timeLimit"
            label="时间限制"
            class="full-width"
          >
            <el-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间限制"
              min="0"
            />
          </el-form-item>
          <el-form-item
            prop="judgeConfig.memoryLimit"
            label="内存限制"
            class="full-width"
          >
            <el-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制"
              min="0"
            />
          </el-form-item>
          <el-form-item
            prop="judgeConfig.stackLimit"
            label="堆栈限制"
            class="full-width"
          >
            <el-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
              min="0"
            />
          </el-form-item>
        </el-form-item>
        <el-form-item label="测试用例配置">
          <el-row v-for="(judgeCaseItem, index) of form.judgeCase" :key="index">
            <el-col :span="24">
              <el-form-item
                :prop="`judgeCase[${index}].input`"
                :label="`输入用例-${index}`"
                class="full-width"
              >
                <el-input
                  type="textarea"
                  v-model="judgeCaseItem.input"
                  placeholder="请输入测试输入用例"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                :prop="`judgeCase[${index}].output`"
                :label="`输出用例-${index}`"
                class="full-width"
              >
                <el-input
                  type="textarea"
                  v-model="judgeCaseItem.output"
                  placeholder="请输入测试输出用例"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24" class="delete-button">
              <el-button
                type="danger"
                @click="handleDelete(index)"
                class="delete-btn"
                >删除</el-button
              >
            </el-col>
          </el-row>
          <div class="add-button">
            <el-button @click="handleAdd" type="success"
              >新增测试用例</el-button
            >
          </div>
        </el-form-item>
        <div class="submit-button">
          <el-form-item>
            <el-button type="primary" @click="doSubmit">提交</el-button>
          </el-form-item>
        </div>
      </el-form>
    </el-card>
  </div>
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
