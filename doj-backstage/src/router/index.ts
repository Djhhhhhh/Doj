import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ACCESS_ENUM from "@/access/accessEnum";
import BasicLayout from "@/layouts/BasicLayout.vue";
import DateView from "@/layouts/DateView.vue";
import LoginView from "@/views/user/LoginView.vue";
import QuestionView from "@/views/question/QuestionView.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import UpdateQuestionView from "@/views/question/UpdateQuestionView.vue";
import QuestionListView from "@/views/question/QuestionListView.vue";
import SubmitQuestionView from "@/views/question/SubmitQuestionView.vue";
import SubmitView from "@/views/question/SubmitView.vue";
import SubmitListView from "@/views/question/SubmitListView.vue";
import UserSetting from "@/views/user/UserSetting.vue";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    component: BasicLayout,
    children: [
      {
        path: "user/login",
        component: LoginView,
      },
      {
        path: "",
        component: DateView,
      },
      {
        path: "question",
        component: QuestionView,
        meta: { access: ACCESS_ENUM.USER },
        children: [
          {
            path: "add",
            component: AddQuestionView,
            meta: { access: ACCESS_ENUM.ADMIN },
          },
          {
            path: "update",
            component: UpdateQuestionView,
            meta: { access: ACCESS_ENUM.ADMIN },
          },
        ],
      },
      {
        path: "question/list",
        component: QuestionListView,
      },
      {
        path: "question/submit",
        component: SubmitQuestionView,
        meta: { access: ACCESS_ENUM.USER },
        children: [
          {
            path: "sub",
            component: SubmitView,
            meta: { access: ACCESS_ENUM.USER },
          },
        ],
      },
      {
        path: "question/submit/list",
        component: SubmitListView,
        meta: { access: ACCESS_ENUM.USER },
        children: [
          {
            path: "information",
            component: SubmitView,
            meta: { access: ACCESS_ENUM.USER },
          },
        ],
      },
      {
        path: "user/updata",
        component: UserSetting,
        meta: { access: ACCESS_ENUM.USER },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
