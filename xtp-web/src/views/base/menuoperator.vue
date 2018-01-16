<template>
    <section>
        <el-button size="small" type="primary" @click="handleAdd"><i class="fa fa-plus"></i> 新增</el-button>
        <!--列表-->
        <el-table :data="dataList" highlight-current-row v-loading="listLoading" style="width: 100%;">
            <el-table-column
                    prop="code"
                    align="center"
                    label="按钮编码">
            </el-table-column>
            <el-table-column
                    prop="name"
                    align="center"
                    label="按钮名称">
            </el-table-column>
            <el-table-column
                    prop="icon"
                    align="center"
                    label="按钮图标">
                <template scope="scope">
                    <i :class="scope.row.icon"></i>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template scope="scope">
                    <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" @close="handleAddCancel">
            <el-form :model="addForm" label-width="100x" :rules="formRules" ref="addForm">
                <el-form-item label="按钮编码" prop="code">
                    <el-input placeholder="请输入按钮编码" v-model="addForm.code"></el-input>
                </el-form-item>
                <el-form-item label="按钮名称" prop="name">
                    <el-input placeholder="请输入按钮名称" v-model="addForm.name"></el-input>
                </el-form-item>
                <el-form-item label="按钮图标" prop="icon">
                    <span style="color: #1c8de0; font-size: 1.5rem"><i :class="addForm.icon"></i></span>
                    <el-button type="success" size="mini" @click.native="iconVisible = true">选择图标</el-button>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="handleAddCancel">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog title="选择图标" v-model="iconVisible" :close-on-click-modal="false">
            <div class="iconbox">
                <div class="item" v-for="item in allIcon" @click="handleIcon(item)">
                    <i :class="item"></i>
                </div>
            </div>
        </el-dialog>
    </section>
</template>
<script>
    import {MenuOperationAPI, getAllIcons} from '../../api/api';
    import {CodeChange, MessageBox} from '../../common/js/util'
    import search from '../../components/search.vue'
    export default{
        name: 'xt-menu-operator',
        components: {search},
        props: {
            menuId: {
                type: Number,
                default: 0,
                require: true
            },

        },
        data(){
            return {
                listLoading: false,
                iconVisible: false,
                addFormVisible: false,
                allIcon: getAllIcons(),
                dataList: [],
                addLoading: false,
                formRules: {
                    code: [
                        {required: true, message: '请输入按钮编码', trigger: 'blur'},
                        {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'},
                    ],
                    name: [
                        {required: true, message: '请输入按钮名称', trigger: 'blur'},
                        {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
                    ],
                    icon: [
                        {required: true, message: '请选择菜单图标', trigger: 'change'},
                    ],
                },
                //新增界面数据
                addForm: {id: 0, menuId: 0, code: '', name: '', icon: ''}
            }
        },
        methods: {
            handleAddCancel(){
                this.addFormVisible = false
                this.$refs["addForm"].resetFields();
                this.addForm = {
                    id: 0,
                    appId: '',
                    parentId: 0,
                    url: '',
                    code: '',
                    name: '',
                    icon: '',
                    sort: 0,
                    displayFlag: true
                }
            },
            handleIcon(icon){
                if (this.editFormVisible) {
                    this.editForm.icon = icon;
                } else {
                    this.addForm.icon = icon;
                }
                this.iconVisible = false;
            },
            //获取菜单按钮列表
            getDataList() {
                let _self = this;
                MenuOperationAPI.listMenuOperation({menuId: _self.menuId, pageIndex: 1, pageSize: 999999}).then(res => {
                    _self.dataList = res.data.data.data;
                });
            },
            //删除
            handleDel(index, row) {
                let _this = this;
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(function () {
                    _this.listLoading = true;
                    let para = {id: row.id};
                    MenuOperationAPI.deleteMenuOperation(para).then(function (res) {
                        if (res.data.code == 1) {
                            _this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            _this.getDataList();
                        } else {
                            MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                if (res.data.msg != '' && res.data.msg != 'undefined')
                                    message = res.data.msg;
                                _this.$message({
                                    message: message,
                                    type: 'error'
                                });
                            })
                        }
                        _this.listLoading = false;
                    });
                }).catch(() => {

                });
            },
            //显示新增界面
            handleAdd: function (obj) {
                this.addFormVisible = true;
                //this.$refs["addForm"].resetFields();
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.addForm);
                            para.menuId=this.menuId;
                            MenuOperationAPI.insertMenuOperation(para).then((res) => {
                                this.addLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible = false;
                                    this.getDataList();
                                } else {
                                    let _this = this;
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                        if (res.data.msg != '' && res.data.msg != 'undefined')
                                            message = res.data.msg;
                                        _this.$message({
                                            message: message,
                                            type: 'error'
                                        });
                                    })
                                }
                            });
                        });
                    }
                });
            },
        },
        created(){
            this.getDataList();
        }
    }
</script>
<style scoped>
    .iconbox {
        display: flex;
        flex-flow: row wrap;
    }

    .iconbox .item {
        padding: .5rem;
        margin: .2rem;
        border: 1px solid #eeeeee;
        font-size: 2rem;
        cursor: pointer;
    }

    .iconbox .item:hover {
        background-color: #73ccff;
    }
</style>