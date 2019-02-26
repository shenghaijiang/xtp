<template>
  <div @keyup.13="handleLogin" id="mainBox">
    <div class="login-container">
      <!--头部-->
      <div class="form-title-wrap">
        <div class="form-title">{{systemName}}</div>
        <div class="form-subtitle">后台登录</div>
        <div class="form-english">BackStage login</div>
      </div>
      <!--表单-->
      <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="rule-form">
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
      </el-form>

      <div class="remember">
        <el-checkbox v-model="IsRemember">记住密码</el-checkbox>
      </div>
      <div class="buttons">
        <el-button type="primary" class="login" @click.native.prevent="handleLogin" :loading="logining">登录</el-button>
        <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
      </div>
    </div>
  </div>
</template>

<script>
 import {UserAPI} from "../../../modules/xtp";

 export default {
  data() {
    return {
      systemName: "xtp通用管理系统",
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
@import '../../../assets/styles/scss/views/base/login/login';
</style>
