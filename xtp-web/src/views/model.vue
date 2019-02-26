<template>
    <section>
        <iframe :id="$route.path" :src="path" style="width: 100%;height:auto;min-height: 800px;border: 0px;overflow-y:hidden;margin-bottom: 2rem"></iframe>
      <!--<html-panel :url.asyc="path"></html-panel>-->
    </section>
</template>
<script>
import { UrlGet } from "../common/js/util";
// import HtmlPanel from "./html-panel";

export default{
  components: {
    // HtmlPanel
  },
  data() {
    return {path: ""};
  },
  watch: {
    "$route.params": {
      immediate: true,
      handler(val) {
        this.getIframePath();
      }
    }
  },
  methods: {
    getIframePath() {
      const params = this.$route.params;
        let srcPath = "", modelPath = "", lastPath = "";
      if (params.modelName) {
        if (params.pageName) {
          if (params.childModelName) {
            modelPath = `/model/${params.modelName}/${params.childModelName}/${params.pageName}`;
            srcPath = `${params.modelName}/#/${params.childModelName}/${params.pageName}`;
            lastPath = `#/${params.childModelName}/${params.pageName}`;
          } else {
            modelPath = `/model/${params.modelName}/${params.pageName}`;
            srcPath = `${params.modelName}/#/${params.pageName}`;
            lastPath = `#/${params.pageName}`;
          }
        } else {
          modelPath = `/model/${params.pageName}`;
          srcPath = `${params.modelName}`;
          lastPath = "";
        }
      }
      let query = "";
      if (this.$route.query) {
        Object.keys(this.$route.query).map((queryItem) => {
          query = `${query}&${queryItem}=${this.$route.query[queryItem]}`;
        });
      }
      this.$store.dispatch("getMenuOperation", this.$route.fullPath || modelPath).then(({operation}) => {
        this.path = `http://${window.APP_SETTING_WEB.SERVER_HOST}:${window.APP_SETTING_WEB.SERVER_PORT}/${params.modelName}/?v=${Math.random()}&xtit_oauth=${localStorage.getItem("isautologin") ? (localStorage.getItem(window.TOKEN_KEY) || "") : (sessionStorage.getItem(window.TOKEN_KEY) || "")}${this.$route.hash ? this.$route.hash : ""}&operation=${operation}${query}${lastPath}`;
      });
      }
  },
  created() {
       // this.getIframePath();
  }

};
</script>
