import { createApp } from "vue";
import App from "./App.vue";
import ArcoVue from "@arco-design/web-vue";
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import { createPinia } from "pinia";
import "@arco-design/web-vue/dist/arco.css";
import router from "./router";
import "@/access";
import axios from "axios";

axios.defaults.withCredentials = true;

const pinia = createPinia();

const app = createApp(App);
app.use(pinia);
app.use(router);
app.use(ArcoVue);
app.use(ArcoVueIcon);
app.mount("#app");
