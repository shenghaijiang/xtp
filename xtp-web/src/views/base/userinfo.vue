<template>
    <section style="padding: .8rem">
        <el-tabs type="card" active-name="base">
            <el-tab-pane label="基础信息" name="base">
                <div @keyup.13="handleUpdateInfo">
                    <el-form v-model="userInfo" label-width="100px" :rules="formRules" ref="userInfo">
                        <el-form-item label="账户名" prop="account">
                            <el-input :disabled="true" v-model="userInfo.account" auto-complete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="userInfo.name" auto-complete="off" :disabled="true"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号" prop="phone">
                            <el-input v-model="userInfo.phone" auto-complete="off"
                                      :disabled="editFormVisible"></el-input>
                        </el-form-item>
                        <el-form-item label="Email" prop="mail">
                            <el-input v-model="userInfo.mail" auto-complete="off"
                                      :disabled="editFormVisible"></el-input>
                        </el-form-item>
                        <el-form-item label="QQ" prop="qq">
                            <el-input v-model="userInfo.qq" auto-complete="off" :disabled="editFormVisible"></el-input>
                        </el-form-item>
                        <el-form-item align="center" v-show="editFormVisible">
                            <el-button type="success" @click="editFormVisible=false" :loading="editInfoLoading">
                                <!--<i class="fa fa-save"></i>--> 修改数据
                            </el-button>
                        </el-form-item>
                        <el-form-item align="center" v-show="!editFormVisible">
                            <el-button @click.native="handleCancel">取消</el-button>
                            <el-button type="success" @click="handleUpdateInfo" :loading="editInfoLoading"><i
                                    class="fa fa-save"></i> 保存数据
                            </el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="setpwd">
                <div @keyup.13="handleUpdatePwd">
                    <el-form :model="settingInfo" label-width="100px" :rules="pwdFormRules" ref="settingInfo">
                        <el-form-item label="原始密码" prop="oldPwd">
                            <el-input type="password" v-model="settingInfo.oldPwd" auto-complete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="新密码" prop="newPwd">
                            <el-input type="password" v-model="settingInfo.newPwd" auto-complete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码" prop="confirmPwd">
                            <el-input type="password" v-model="settingInfo.confirmPwd" auto-complete="off"></el-input>
                        </el-form-item>
                        <el-form-item align="center">
                            <el-button type="success" @click="handleUpdatePwd" :loading="editPwdLoading"><i
                                    class="fa fa-save"></i> 保存数据
                            </el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-tab-pane>
        </el-tabs>

    </section>
</template>
<script>
    import {UserAPI} from '../../api/api'
    import {CheckExp, MessageBox} from '../../common/js/util'

    export default{
        name: 'userinfo',
        data(){
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入新密码'));
                } else if (value.length < 6 || value.length > 20) {
                    callback(new Error('长度在 6 到 20 个字符'));
                } else {
                    if (this.settingInfo.newPwd !== '') {
                        this.$refs.settingInfo.validateField('confirmPwd');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入确认密码'));
                } else if (value.length < 6 || value.length > 20) {
                    callback(new Error('长度在 6 到 20 个字符'));
                } else if (value !== this.settingInfo.newPwd) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            var checkPhone = (rule, value, callback) => {
                if (CheckExp.isTel(value)) {
                    callback();
                } else {
                    return callback(new Error('请填写正确的电话号码'));
                }
            }
            var checkQQ = (rule, value, callback) => {
                if (value != '' && value != undefined) {
                    if (CheckExp.isQQ(value)) {
                        callback();
                    } else {
                        return callback(new Error('请填写正确的QQ'));
                    }
                } else {
                    callback();
                }
            }
            return {
                editInfoLoading: false,
                editPwdLoading: false,
                userInfo: {account: '', name: '',phone:'***********',mail:'',qq:''},
                settingInfo: {oldPwd: '', newPwd: '', confirmPwd: ''},
                formRules: {
                    account: [
                        {required: true, message: '请输入账户名', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    phone: [
                        {minlength: 11, maxlength: 11, required: true, message: '请输入正确的手机号', trigger: 'blur'},
                        {validator: checkPhone, trigger: 'blur,change'}],
                    mail: [{required: false, message: '', trigger: 'blur'},
                        {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change'}],
                    qq: [{required: false, message: '', trigger: 'blur'}, {validator: checkQQ, trigger: 'blur,change'}]
                },
                pwdFormRules: {
                    oldPwd: [
                        {required: true, message: '请输入原始密码', trigger: 'blur'},
                        {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
                    ],
                    newPwd: [
                        {required: true, validator: validatePass, trigger: 'blur'}
                    ],
                    confirmPwd: [
                        {required: true, validator: validatePass2, trigger: 'blur'}
                    ]
                }, editFormVisible: true
            }
        },
        methods: {
            handleCancel(){
                this.$refs['userInfo'].resetFields();
                this.editFormVisible = true
                let _self = this;
                getUserInfo().then(function (res) {
                    _self.userInfo = res.data.data;
                })
            },
            handleUpdatePwd(){
                let _this = this;
                this.$refs.settingInfo.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认修改密码吗？', '提示', {}).then(() => {
                            this.editPwdLoading = true;
                            //NProgress.start();
                            let para = {password: this.settingInfo.newPwd, account: this.userInfo.account};
                            let oldpwd = this.settingInfo.oldPwd;
                            UserAPI.editUserPassword(para, oldpwd).then((res) => {
                                this.editPwdLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '密码修改成功，请重新登录',
                                        type: 'success'
                                    });
                                    localStorage.removeItem('xtit_ouath');
                                    this.$router.push('/login');
                                } else {
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                        _this.$message({
                                            message: message,
                                            type: 'error'
                                        });
                                    })
                                }
                                this.$refs['settingInfo'].resetFields();
                            });
                        });
                    }
                });
            },
            handleUpdateInfo(){
                let _this = this;
                this.$refs.userInfo.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认保存吗？', '提示', {}).then(() => {
                            this.editInfoLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.userInfo);
                            UserAPI.editUserInfo(para).then((res) => {
                                this.editInfoLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '保存成功',
                                        type: 'success'
                                    });
                                    this.editFormVisible = true
                                } else {
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                        _this.$message({
                                            message: message,
                                            type: 'error'
                                        });
                                    })
                                    this.$refs['userInfo'].resetFields();
                                    let _self = this;
                                    UserAPI.getUserInfo().then(function (res) {
                                        _self.userInfo = res.data.data;
                                    })
                                }
                            });
                        });
                    }
                });
            }
        },
        mounted(){
            let _self = this;
            UserAPI.getUserInfo().then(function (res) {
                console.log(res);
                _self.userInfo = res.data.data;
            })
        }
    }
</script>
<style scoped></style>