<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import { onMounted, ref, watch } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";
import moment from "moment";
import { QuestionControllerService } from "@/api/apiQuestion";

const router = useRouter();
const route = useRoute();
const onBack = () => {
  router.back();
};

let form = ref({
  id: "",
  language: "",
  code: "",
  status: "",
  questionId: "",
  judgeInfo: {
    message: "Waitng",
    time: 1000,
    memory: 1000,
  },
  updateTime: "",
});
const isDataLoaded = ref(false);

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
const loadQuestion = async () => {
  const id = form.value.questionId;
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
  }
};

// 加载数据函数
const loadData = async () => {
  const id = route.query.id;
  const res = await QuestionControllerService.getQuestionSubmitVoByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    isDataLoaded.value = true;
  } else {
    alert("加载失败，" + res.message);
  }
};

// 组件加载时执行
onMounted(async () => {
  await loadData();
  loadQuestion();
});

const handleStateClick = async () => {
  console.log(form.value.judgeInfo.message);
  await loadData();
  loadQuestion();
};

const routeGo = (i: string) => {
  router.push({
    path: "/question/submit",
    query: {
      id: i,
    },
  });
};
</script>

<template>
  <div class="sub-page" @click="onBack">
    <div class="card-wrapper" @click.stop>
      <el-card class="card">
        <template #header>
          <el-page-header @back="onBack">
            <template #content>
              <span @click="routeGo(form.questionId)" style="cursor: pointer">
                {{ question.title }}
              </span>
            </template>
          </el-page-header>
        </template>
        <el-descriptions border style="margin-bottom: 20px">
          <el-descriptions-item label="评测编号">
            {{ form.id }}
          </el-descriptions-item>
          <el-descriptions-item label="评测状态">
            {{ form.judgeInfo.message }}
          </el-descriptions-item>
          <el-descriptions-item label="提交时间">
            {{ moment(form.updateTime).format("MM-DD hh:mm:ss") }}
          </el-descriptions-item>
          <el-descriptions-item label="语言">
            {{ form.language }}
          </el-descriptions-item>
          <el-descriptions-item label="时间">
            {{ form.judgeInfo.time }}
          </el-descriptions-item>
          <el-descriptions-item label="空间">
            {{ form.judgeInfo.memory }}
          </el-descriptions-item>
        </el-descriptions>
        <div style="height: 40vh">
          <CodeEditor
            v-if="isDataLoaded"
            :value="form.code as string"
            :language="form.language"
          />
        </div>
      </el-card>
      <div
        class="state"
        :style="{
          color:
            form.judgeInfo.message === 'Accepted'
              ? 'green'
              : form.judgeInfo.message === 'Time limit exceeded'
              ? 'blue'
              : form.judgeInfo.message === 'Wrong Answer'
              ? 'red'
              : 'black',
        }"
      >
        <span
          v-if="
            form.judgeInfo.message === 'Waitng' ||
            form.judgeInfo.message === null ||
            form.judgeInfo.message === undefined
          "
          class="refresh-state"
          @click="handleStateClick"
        >
          点击刷新评测状态
        </span>
        <span v-else>
          {{ form.judgeInfo.message }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.sub-page {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
  background: rgba(255, 255, 255, 0.1);
}
.card-wrapper {
  position: relative;
  width: 50%;
}
.state {
  position: absolute;
  top: -3%;
  right: -5%;
  transform: rotate(0.03turn);
  font-size: xx-large;
}
.refresh-state {
  cursor: pointer;
  transition: color 0.5s ease;
}

.refresh-state:hover {
  animation: rainbow 3s infinite;
}

@keyframes rainbow {
  0% {
    color: black;
  }
  14% {
    color: orange;
  }
  28% {
    color: yellow;
  }
  42% {
    color: green;
  }
  57% {
    color: blue;
  }
  71% {
    color: indigo;
  }
  85% {
    color: violet;
  }
  100% {
    color: black;
  }
}
</style>
