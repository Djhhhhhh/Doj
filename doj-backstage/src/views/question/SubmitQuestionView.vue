<script setup lang="ts">
import { onMounted, ref, watchEffect, withDefaults, defineProps } from "vue";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import { useRoute, useRouter } from "vue-router";
import { marked } from "marked";

const route = useRoute();
const router = useRouter();
import {
  QuestionControllerService,
  QuestionSubmitAddDTO,
} from "@/api/apiQuestion";

let question = ref({
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
const loadData = async () => {
  const id = route.query.id;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    question.value = res.data as any;
    // json 转 js 对象
    if (!question.value.judgeCase) {
      question.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      question.value.judgeCase = JSON.parse(question.value.judgeCase as any);
      console.log(question.value.judgeCase);
    }
    // 分离题目正文中的 "输入描述" 和 "输出描述"
    const content = question.value.content || "";
    const inputPattern = /输入描述\s*([\s\S]*?)(?=输出描述|$)/;
    const outputPattern = /输出描述\s*([\s\S]*)/;

    const inputMatch = content.match(inputPattern);
    const outputMatch = content.match(outputPattern);
    // 去掉输入描述和输出描述后的内容
    question.value.content = question.value.content
      .replace(inputPattern, "")
      .replace(outputPattern, "");
    question.value.inputDescription = inputMatch ? inputMatch[1].trim() : "";
    question.value.outputDescription = outputMatch ? outputMatch[1].trim() : "";
    question.value.judgeCase = await Promise.all(
      question.value.judgeCase.map(async (judgeCase) => {
        // 处理 input 和 output 解析
        judgeCase.input = formatText(judgeCase.input);
        judgeCase.Output = formatText(judgeCase.Output);
        return judgeCase;
      })
    );
    if (!question.value.judgeConfig) {
      question.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      question.value.judgeConfig = JSON.parse(
        question.value.judgeConfig as any
      );
    }
    if (!question.value.tags) {
      question.value.tags = [];
    } else {
      question.value.tags = JSON.parse(question.value.tags as any);
    }
  } else {
    message.error("加载失败，" + res.message);
  }
};

const form = ref<QuestionSubmitAddDTO>({
  language: "cpp",
  code: "",
});

onMounted(() => {
  loadData();
});

const changeCode = (value: string) => {
  form.value.code = value;
};

const doSubmit = async () => {
  const id = route.query.id;
  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: id,
  });
  router.push({
    path: "/question/submit/sub",
    query: {
      id: res.data,
    },
  });
};
const formatText = (text: string) => {
  return text ? text.replace(/\n/g, "<br/>") : "";
};

const formatMarkdown = (markdown: string) => {
  // 使用 marked 将 Markdown 转换为 HTML
  return marked(markdown || "");
};
</script>
<template>
  <el-container style="height: 100%">
    <el-aside class="aside">
      <el-card class="card">
        <template #header>
          <div>
            <h1>{{ question?.title }}</h1>
            <p class="text">TimeLimit：{{ question.judgeConfig.timeLimit }}</p>
            <p class="text">
              MemoryLimit：{{ question.judgeConfig.memoryLimit }}
            </p>
          </div>
        </template>
        <div class="content" v-html="formatMarkdown(question?.content)"></div>
        <hr />
        <div v-if="question.inputDescription" class="description-section">
          <h4>输入描述</h4>
          <div v-html="formatMarkdown(question.inputDescription)"></div>
        </div>
        <hr />
        <div v-if="question.outputDescription" class="description-section">
          <h4>输出描述</h4>
          <div v-html="formatMarkdown(question.outputDescription)"></div>
        </div>
        <template #footer>
          <div
            v-for="(item, index) in question.judgeCase.slice(0, 2)"
            :key="index"
          >
            <h4 style="margin: 0; padding: 0">test:{{ index + 1 }}</h4>
            <div style="display: flex; margin-bottom: 10px">
              <el-card style="margin-right: 10px; width: 45%">
                <div v-html="item.input" class="formatted-text" />
              </el-card>
              <el-card style="width: 50%">
                <div v-html="item.Output" class="formatted-text" />
              </el-card>
            </div>
          </div>
        </template>
      </el-card>
    </el-aside>
    <el-main>
      <el-card>
        <template #header>
          <div class="codehead">
            <el-select
              v-model="form.language"
              style="width: 40%; min-width: 200px"
            >
              <el-option value="java">java</el-option>
              <el-option value="cpp">cpp</el-option>
            </el-select>
            <el-button type="primary" plain class="btn">测试</el-button>
            <el-button type="primary" plain @click="doSubmit()">提交</el-button>
          </div>
        </template>
        <CodeEditor
          :value="form.code as string"
          :language="form.language"
          :handle-change="changeCode"
        />
      </el-card>
    </el-main>
  </el-container>
  <router-view />
</template>

<style scoped>
.aside {
  height: auto;
  width: 45%;
  .card {
    height: auto;
    .text {
      font-size: small;
      margin-bottom: 0px;
      padding: 0;
    }
  }
}
.btn {
  margin-left: 5%;
}
.formatted-text {
  white-space: pre-wrap; /* 保持换行符和多余的空格 */
  letter-spacing: 1px; /* 调整字符之间的间距 */
  line-height: 1.4; /* 设置行高，增加每行之间的间距 */
  font-family: "Courier New", monospace; /* 使用等宽字体，使字符画效果更好 */
  font-size: 14px; /* 设置字体大小，可以根据需要调整 */
  word-break: break-word; /* 确保长单词在需要时换行 */
  padding: 10px; /* 添加内边距，让内容不贴着边缘 */
  border: 1px solid #ddd; /* 添加边框，增加视觉层次 */
  border-radius: 4px; /* 边框圆角 */
}
</style>
