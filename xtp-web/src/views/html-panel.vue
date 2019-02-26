<template>
  <div>
    <!--<mu-circular-progress :size="40" v-if="loading"/>-->
    <div v-html="html"></div>
  </div>
</template>
<style>

</style>
<script>
  import {_axios} from "../modules/api";

  export default{
    // 使用时请使用 :url.sync=""传值
    props: {
      url: {
        required: true
      }
    },
    data () {
      return {
        loading: false,
        html: ""
      };
    },
    watch: {
      url (value) {
        this.load(value);
      }
    },
    mounted () {
      this.load(this.url);
    },
    methods: {
      load (url) {
        if (url && url.length > 0) {
          // 加载中
          this.loading = true;
          const param = {
            // accept: "text/html, text/plain"
            "Content-Type": "application/x-www-form-urlencoded",
            responseType: "json"
          };
          console.log(_axios, url);
          _axios.get(url, param).then((response) => {
            console.log(response);
            this.loading = false;
            // 处理HTML显示
            this.html = response.data;
          })
.catch(() => {
            this.loading = false;
            this.html = "加载失败";
          });
        }
      }
    }
  };
</script>
