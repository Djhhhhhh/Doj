import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ArcoVue from "@arco-design/web-vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "@arco-design/web-vue/dist/arco.css";
import "@/plugins/axios";
import "@/access";
import "bytemd/dist/index.css";
import "@/assets/css/global.css";

createApp(App)
  .use(store)
  .use(router)
  .use(ArcoVue)
  .use(ElementPlus)
  .mount("#app");
