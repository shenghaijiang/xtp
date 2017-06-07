<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.code" placeholder="应用代码"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="应用名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="default" v-on:click="getApps">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="appList" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
            <!--<el-table-column type="selection" width="55">-->
            <!--</el-table-column>-->
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column
                    prop="code"
                    align="center"
                    label="应用代码">
            </el-table-column>
            <el-table-column
                    prop="name"
                    align="center"
                    label="应用名称">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="Token"
            >
                <template scope="scope">
                    **************
                    <el-button size="small" @click="showToken(scope.$index,scope.row)">显示</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template scope="scope">
                    <el-button size="small" type="warning" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
            <!--<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>-->
            <el-pagination
                    style="float:right;"
                    @size-change="handledSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageInfo.pageIndex"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="pageInfo.pageSize"
                    layout="sizes, prev, pager, next"
                    :total="pageInfo.count">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="100px" :rules="editFormRules" ref="editForm">
                <el-form-item label="应用编码" prop="code">
                    <el-input v-model="editForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="应用名称" prop="name">
                    <el-input v-model="editForm.name" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="100px" :rules="addFormRules" ref="addForm">
                <el-form-item label="应用编码" prop="code">
                    <el-input v-model="addForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="应用名称" prop="name">
                    <el-input v-model="addForm.name" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
    </section>
</template>
<script>
    import util from '../../common/js/util'
    //import NProgress from 'nprogress'
    import { AppAPI } from '../../api/api';

    export default {
        data() {
            return {
                filters: {
                    name: '',
                    code:''
                },
                pageInfo:{pageIndex:1,pageSize:10,count:0},
                appList: [],
                selectedApp:'',
                listLoading: false,
                sels: [],//列表选中列
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    code: [
                        {required: true, message: '请输入应用编码', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入应用名称', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    name: '',
                    code:''
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    code: [
                        {required: true, message: '请输入应用编码', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入应用名称', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    name: '',
                    code: '',
                    id:0
                },
                showBtn:'显示'

            }
        },
        methods: {
            handledSizeChange(val){
                this.pageInfo.pageSize=val;
                this.getApps();
            },
            handleCurrentChange(val){
                this.pageInfo.pageIndex=val;
                this.getApps();
            },
            //获取应用列表
            getApps() {
                let _self = this;
                let para = Object.assign({},this.pageInfo);
                para.name='%'+_self.filters.name+'%';
                para.code='%'+_self.filters.code+'%';
                this.listLoading = true;
                //NProgress.start();
                AppAPI.getAppList(para).then((res) => {
                    _self.pageInfo.pageIndex=res.data.data.currentPage
                    _self.pageInfo.count=res.data.data.count
                    _self.appList = res.data.data.data;
                    if (_self.filters.selected == ''){
                        _self.filters.selected=_self.appList[0].id;
                        para.appId = _self.filters.selected;
                    }
                _self.listLoading = false;
                //NProgress.done();
                });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                //NProgress.start();
                let para = { id: row.id };
                    AppAPI.deleteAppInfo(para).then((res) => {
                    this.listLoading = false;
                //NProgress.done();
                this.$message({
                    message: '删除成功',
                    type: 'success'
                });
                this.getApps();
            });
            }).catch(() => {

                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
            },
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    name: '',
                    code:''
                };
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                        //NProgress.start();
                        let para = Object.assign({}, this.editForm);
                            AppAPI.editAppInfo(para).then((res) => {
                            this.editLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '提交成功',
                            type: 'success'
                        });
                        this.$refs['editForm'].resetFields();
                        this.editFormVisible = false;
                        this.getApps();
                    });
                    });
                    }
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                        //NProgress.start();
                        let para = Object.assign({}, this.addForm);
                            AppAPI.addAppInfo(para).then((res) => {
                            this.addLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '提交成功',
                            type: 'success'
                        });
                        this.$refs['addForm'].resetFields();
                        this.addFormVisible = false;
                        this.getApps();
                    });
                    });
                    }
                });
            },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //批量删除
            batchRemove: function () {
                var ids = this.sels.map(item => item.id).toString();
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                //NProgress.start();
                let para = { ids: ids };
                    AppAPI.deleteAppInfo(para).then((res) => {
                    this.listLoading = false;
                //NProgress.done();
                this.$message({
                    message: '删除成功',
                    type: 'success'
                });
                this.getApps();
            });
            }).catch(() => {

                });
            },
            selectedChange(){
                this.getApps();
            },
            showToken(index,row){
                this.$message({
                    message:row.name+':'+row.token,
                    showClose:true
                })
            }
        },
        mounted() {
            this.getApps();
        }
    }

</script>