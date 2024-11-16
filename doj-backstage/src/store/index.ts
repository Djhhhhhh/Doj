import { createStore } from "vuex";
import user from "./user";

export default createStore({
  state: () => ({
    all: [],
    activeMenuItem: "1",
  }),
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    user,
  },
});
