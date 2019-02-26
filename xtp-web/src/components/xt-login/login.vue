<template>
    <div @keyup.13="handleLogin" class="mainBox">
    <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleForm login-container">
      <div class="form-title-wrap">
        <h1>{{projectName}}</h1>
        <h4 class="form-subtitle">{{systemName}}</h4>
      </div>
      <div class="user">
        <i class="fa fa-user-circle-o user__avatar"></i>
      </div>
      <el-form-item prop="account">
        <el-input type="text" v-model="ruleForm2.account" icon="" auto-complete="off" placeholder="账号">
          <template slot="prepend">
            <i class="fa fa-user-o"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="checkPass">
        <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码">
          <template slot="prepend">
            <i class="fa fa-key"></i>
          </template>
        </el-input>
      </el-form-item>
      <el-checkbox v-model="IsRemember" class="remember">记住密码</el-checkbox>
      <el-form-item style="width:100%;margin-top: -20px">
        <el-button type="primary" style="width:100%;" @click.native.prevent="handleLogin" :loading="logining">登录</el-button>
        <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
 import {UserAPI} from "../../modules/xtp";

 export default {
  data() {
    return {
      projectName:window.PROJECT_NAME,
      systemName: window.SYSTEM_NAME,
      logining: false,
      ruleForm2: {
        account: "",
        checkPass: ""
      },
      rules2: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" }
        ],
        checkPass: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ]
      },
      IsRemember: false
    };
  },
  methods: {
    handleLogin(ev) {
      const _this = this;
      this.$refs.ruleForm2.validate((valid) => {
        if (valid) {
          this.logining = true;
          const loginParams = { account: this.ruleForm2.account, password: this.ruleForm2.checkPass };
          UserAPI.loginUser(loginParams).then(({res}) => {
            sessionStorage.clear();
            localStorage.clear();
            this.logining = false;
            const { msg, code, data } = res.data;
            if (code !== 1) {
              this.$message({
                message: "登录失败,请确认用户名密码！",
                type: "error"
              });
            } else {
              this.saveItem(data);
              this.getUserData();
              this.$store.dispatch("delMenuList", []);
              this.$store.dispatch("delMenuOperation", []);
              this.$store.dispatch("delAllViews", []);
              // this.$store.dispatch('getMenuList',{},true)
            }
          })
          .catch(() => {
            _this.logining = false;
            _this.$message({
                message: "与服务器连接错误,请检查服务器并刷新页面！",
                type: "error"
              });
          });
        } else {
          return false;
        }
      });
    },
    redirect() {
      let redirect = this.$route.query.redirect;
      redirect = redirect ? redirect : "/";
      this.$router.push({ path: redirect });
    },
    saveItem(data) {
      if (this.IsRemember) {
        localStorage.setItem(window.TOKEN_KEY, data);
      } else {
        sessionStorage.setItem(window.TOKEN_KEY, data);
      }
    },
    getUserData () {
      UserAPI.getUser().then(({res}) => {
        if (res.data.code === 1) {
          localStorage.setItem("account", res.data.data.account);
          localStorage.setItem("userName", res.data.data.name);
          sessionStorage.setItem("account", res.data.data.account);
          sessionStorage.setItem("userName", res.data.data.name);
          this.redirect();
        } else {
          this.$message({
            type: "error",
            message: "未能成功获取用户信息，请重新登录"
          });
          return true;
        }
      });
    }
  }, created() {
    this.$message.closeAll();
    try {
      this.systemName = window.SYSTEM_NAME || this.systemName;
    } catch (e) {
      console.warn(e);
    }
  }
};

</script>

<style lang="scss" scoped>
@import './css/login.scss';
</style>
