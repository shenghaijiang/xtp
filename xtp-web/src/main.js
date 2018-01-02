import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import VueRouter from 'vue-router'
// import Vuex from 'vuex'
// import stores from './vuex/store'
import routes from './routes'
import 'font-awesome/css/font-awesome.min.css'
import 'animate.css/animate.min.css'
import babelpolyfill from 'babel-polyfill'

Vue.use(ElementUI)
Vue.use(VueRouter)
// Vue.use(Vuex)

const router = new VueRouter({
  routes
})

// const store=new Vuex.Store({
//   modules:stores
// })

router.beforeEach((to, from, next) => {
  //NProgress.start();
  if (to.path == '/login') {

      sessionStorage.clear();
      localStorage.clear();
  }
  let oauth = localStorage.getItem('isautologin')?(localStorage.getItem("xtit_oauth") || ""):(sessionStorage.getItem("xtit_oauth") || "");
  if (!oauth && to.path != '/login') {
    setTimeout(()=>{
      let oauth = localStorage.getItem('isautologin')?(localStorage.getItem("xtit_oauth") || ""):(sessionStorage.getItem("xtit_oauth") || "");
      if(oauth==="")
        next({ path: '/login' })
      else
        next();
    },50);
  } else {
    if(to.path != '/')
      next()
      else{
      //location.href="/main.html"
        next()
    }
  }
})


var vm=new Vue({
  router,
  // store,
  render: h => h(App)
}).$mount('#app')

