<template>
  <div @keyup.13="handleLogin" class="mainBox">
    <div class="title"></div>
  <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <div class="systitle">xtp通用权限管理系统</div>
    <h3 class="title">
      <i style="color: #0ea0eb;font-size: 8rem" class="fa fa-user-circle-o"></i>
      <br/>
    </h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="ruleForm2.account" icon="" auto-complete="off" placeholder="账号">
        <template slot="prepend"><i class="fa fa-user-o"></i></template>
      </el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码">
        <template slot="prepend"><i class="fa fa-key"></i></template>
      </el-input>
    </el-form-item>
    <el-checkbox v-model="IsRemember" class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click.native.prevent="handleLogin" :loading="logining">登录</el-button>
      <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
    </el-form-item>
  </el-form>
    <div class="title"></div>
  </div>
</template>

<script>
  import { requestLogin } from '../api/api';
  //import NProgress from 'nprogress'
  export default {
    data() {
      return {
        logining: false,
        ruleForm2: {
          account: '',
          checkPass: ''
        },
        rules2: {
          account: [
            { required: true, message: '请输入账号', trigger: 'blur' },
            //{ validator: validaePass }
          ],
          checkPass: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            //{ validator: validaePass2 }
          ]
        },
        IsRemember: false
      };
    },
    methods: {
      handleLogin(ev) {
        var _this = this;
        this.$refs.ruleForm2.validate((valid) => {
          if (valid) {
            this.logining = true;
            var loginParams = { account: this.ruleForm2.account, password: this.ruleForm2.checkPass };
            requestLogin(loginParams).then(result => {
              this.logining = false;
              let { msg, code,data } = result.data;
              if (code !== 1) {
                this.$message({
                  message: '登录失败,请确认用户名密码！',
                  type: 'error'
                });
              } else{
                  localStorage.setItem("user",this.ruleForm2.account);
                  if(!this.IsRemember)
                    sessionStorage.setItem('xtit_oauth', data);
                  else {
                      localStorage.setItem('isautologin',"1");
                      localStorage.setItem('xtit_oauth', data);
                      console.log(JSON.stringify(localStorage))
                  }
                location.href='./';
             }
            }).catch(function (error) {
              _this.logining = false;
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
    created(){
        if(!localStorage.getItem('isautologin')){
            if(!localStorage.getItem('xtit_oauth'))
                this.$router.push({ path: '/' });
        }
        else if(sessionStorage.getItem("xtit_oauth")){
          this.$router.push({ path: '/' });
      }
    }, mounted(){

      }
  }

</script>

<style lang="scss" scoped>
  .mainBox{
    width: 100%;
    height: 100%;
    background-color: #1c8de0;
    position: relative;
  }
  .mainBox .title
  {
    height: 3rem;
    line-height: 3rem;
    text-align: center;
    font-size: 1.5rem;
    color: #000000;
  }
  .mainBox .systitle
  {
    height: 4rem;
    line-height: 4rem;
    text-align: center;
    font-size: 1.5rem;
    color: #505458;
  }
  .login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    position: absolute;
    margin: auto;
    top: 0; left: 0; bottom: 0; right: 0;
    width: 20%;
    height: 40%;
    /*top:25%;*/
    /*bottom: 25%;*/
    /*left: 30%;*/
    /*right: 30%;*/
    min-height: 26rem;
    min-width: 18.5rem;
    /*margin-top: -227px;*/
    /*margin-left: -210px;*/
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 90px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>