// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import Router from "vue-router";
import routers from "./router";
import store from "./store";
// import './assets/styles/reset.scss'
import "font-awesome/css/font-awesome.min.css";
import "./assets/styles/scss/element-variables.scss";
import ElementUI from "element-ui";
import components from "./components";
import "babel-polyfill";

Vue.use(Router);
Vue.use(ElementUI);
Vue.use(components);

Vue.config.productionTip = false;

window.document.title = window.SYSTEM_NAME;
const router = new Router({
  routes: routers
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    const getStorageItem = localStorage.getItem(window.TOKEN_KEY) || sessionStorage.getItem(window.TOKEN_KEY) || null;
    if (!getStorageItem) {
      next({
        path: "/login"
        // query: { redirect: to.fullPath }
      });
    } else {
      next();
    }
    next();
  } else {
    next();
  }
});

/* eslint-disable no-new */
window._Vue = new Vue({
  el: "#app",
  store,
  router,
  template: "<App/>",
  components: { App },
  data: {
    Bus: new Vue()
  }
});
