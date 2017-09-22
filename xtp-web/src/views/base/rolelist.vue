<template>
    <section>
        <!--工具条-->
        <xt-search @click="getRoles" :addButton="true" @addClick="handleAdd">
            <el-form :inline="true" :model="filters">
                <el-select v-model="filters.selected" placeholder="请选择" @change="selectedChange">
                    <el-option
                            v-for="item in appList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="角色名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.code" placeholder="角色编码"></el-input>
                </el-form-item>
            </el-form>
        </xt-search>
        <!--列表-->
        <el-table :data="roleList" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
                  style="width: 100%;">
            <el-table-column type="expand">
                <template scope="scope">
                    <rolemenu :roleId="scope.row.id" :appId="scope.row.appId"></rolemenu>
                </template>
            </el-table-column>
            <el-table-column type="index" width="60">
                <template scope="scope">
                    {{(pageInfo.pageIndex-1)*pageInfo.pageSize+scope.$index+1}}
                </template>
            </el-table-column>
            <el-table-column
                    prop="code"
                    align="center"
                    label="角色编码">
            </el-table-column>
            <el-table-column
                    prop="name"
                    align="center"
                    label="角色名称">
            </el-table-column>
            <el-table-column
                    prop="createDate"
                    align="center"
                    label="创建时间">
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
            <el-form :model="editForm" label-width="100px" :rules="formRules" ref="editForm">
                <el-form-item label="角色编码" prop="code">
                    <el-input v-model="editForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色名称" prop="name">
                    <el-input v-model="editForm.name" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="handleCancel">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
            <el-form :model="addForm" label-width="100px" :rules="formRules" ref="addForm">
                <el-form-item label="角色编码" prop="code">
                    <el-input v-model="addForm.code" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色名称" prop="name">
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
    import {AppAPI,RoleAPI} from '../../api/api';
    import rolemenu from './rolemenu';
    import search from '../../components/search.vue'
    import {CodeChange, MessageBox, util} from '../../common/js/util'

    export default {
        components: {rolemenu, 'xt-search': search},
        data() {
            var checkCode = (rule, value, callback) => {
                let param = {code: value}
                this.getHadRole(param).then(({isempty, id}) => {
                    if ((this.editFormVisible) && (id == this.editForm.id))
                        callback();
                    else {
                        if (!isempty)
                            return callback(new Error('该角色编码已存在，请重新输入'));
                        else callback();
                    }
                })
            }
            var checkName = (rule, value, callback) => {
                let param = {name: value}
                this.getHadRole(param).then(({isempty, id}) => {
                    if ((this.editFormVisible) && (id == this.editForm.id))
                        callback()
                    else {
                        if (!isempty)
                            return callback(new Error('该角色名称已存在，请重新输入'));
                        else callback();
                    }
                })
            }
            return {
                filters: {
                    key: '', code: '', name: '',selected:''
                },
                appList:[],
                pageInfo: {pageIndex: 1, pageSize: 10, count: 0},
                roleList: [],
                listLoading: false,
                sels: [],//列表选中列
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                formRules: {
                    code: [
                        {required: true, message: '请输入角色编码', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
                        {validator: checkCode, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'},
                        {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
                        {validator: checkName, trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    id: 0,
                    name: '',
                    code: ''
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                //新增界面数据
                addForm: {
                    name: '',
                    code: '',
                    id: 0
                }

            }
        },
        watch: {
            'addForm.code'(){
                this.addForm.code = this.addForm.code.replace(/(^\s+)|(\s+$)/g, "")
            },
            'addForm.name'(){
                this.addForm.name = this.addForm.name.replace(/(^\s+)|(\s+$)/g, "")
            },
            'editForm.code'(){
                this.editForm.code = this.editForm.code.replace(/(^\s+)|(\s+$)/g, "")
            },
            'editForm.name'(){
                this.editForm.name = this.editForm.name.replace(/(^\s+)|(\s+$)/g, "")
            }
        },
        methods: {
            handleCancel(){
                this.editFormVisible = false
                this.$refs["editForm"].resetFields();
            },
            handledSizeChange(val){
                this.pageInfo.pageSize = val;
                this.getRoles();
            },
            handleCurrentChange(val){
                this.pageInfo.pageIndex = val;
                this.getRoles();
            },
            //获取角色列表
            getRoles() {
                let _self = this;
                let para = Object.assign({}, this.pageInfo);
                para.name = '%' + _self.filters.name + '%';
                para.code = '%' + _self.filters.code + '%';
                if (_self.filters.selected != ''){
                    para.appId = _self.filters.selected;
                }
                this.listLoading = true;
                //NProgress.start();
                RoleAPI.listRole(para).then((res) => {
                    _self.pageInfo.pageIndex = res.data.data.currentPage
                    _self.pageInfo.count = res.data.data.count
                    _self.roleList = res.data.data.data;
                    _self.roleList.map(element => {
                        element.code = CodeChange.specialCharacter(element.code)
                        element.name = CodeChange.specialCharacter(element.name)
                    })
                    _self.listLoading = false;
                    //NProgress.done();
                }).catch(function (error) {
                    _self.listLoading = false;
                    _self.roleList = []
                });
            },
            getHadRole(params){
                return new Promise(function (resolve, reject) {
                    let isempty = true, id = '';
                    RoleAPI.listRole(params).then((res) => {
                        if (res.data.data.count != 0) {
                            isempty = false
                            id = res.data.data.data[0].id
                        }
                        resolve({isempty, id})
                    })
                })
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = {id: row.id};
                    RoleAPI.deleteRole(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getRoles();
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
                this.addForm = {name: '', code: '', id: 0}
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.editForm);
                            RoleAPI.updateRole(para).then((res) => {
                                this.editLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['editForm'].resetFields();
                                    this.editFormVisible = false;
                                    this.getRoles();
                                } else {
                                    let _this = this;
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
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
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.addForm);
                            RoleAPI.insertRole(para).then((res) => {
                                this.addLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible = false;
                                    this.getRoles();
                                } else {
                                    let _this = this;
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
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
            getApps() {
                let _self = this;
                let para = Object.assign({},this.pageInfo);
                para.name='%'+_self.filters.key+'%';
                if(_self.filters.selected!='' ){
                    para.appId = _self.filters.selected;
                }
                this.listLoading = true;
                //NProgress.start();
                AppAPI.listApp(para).then((res) => {
                    _self.pageInfo.pageIndex=res.data.data.currentPage
                    _self.pageInfo.count=res.data.data.count
                    _self.appList = res.data.data.data;
                    _self.appList.map(function (item) {
                        item.tokenshow='**************';
                    })
                    if (_self.filters.selected == ''){
                        _self.filters.selected=_self.appList[0].id;
                        para.appId = _self.filters.selected;
                    }
                    _self.listLoading = false;
                    //NProgress.done();
                });
            },
            selectedChange(){
                this.getRoles();
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
                    let para = {ids: ids};
                    RoleAPI.deleteRole(para).then((res) => {
                        this.listLoading = false;
                        if (res.data.code == 1) {
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            this.getApps();
                        } else {
                            let _this = this;
                            MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                _this.$message({
                                    message: message,
                                    type: 'error'
                                });
                            })
                        }
                    });
                }).catch(() => {

                });
            }
        },
        mounted() {
            this.getApps();
//            this.getRoles();
        }
    }

</script>