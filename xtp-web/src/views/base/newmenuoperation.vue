<template>
    <section>
         <!--工具条-->
        <xt-search @click="handleSearch" :addButton="false" >
            <el-form :inline="true" :model="filters">
                <el-select v-model="filters.appId" placeholder="请选择">
                    <el-option
                            v-for="item in appList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="菜单名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.code" placeholder="菜单编码"></el-input>
                </el-form-item>
            </el-form>
        </xt-search>

        <!--列表-->
        <div class="xt-body">
            <div class="xt-left">
                <div class="xt-left-head">
                    菜单列表
                </div>
                <div class="xt-left-list">
                     <el-tree :data="menuList" :props="defaultProps" style="height: 650px;overflow: auto;border:none;"
                             :highlight-current="true" lazy :load="loadNode"
                             :render-content="renderContent"
                             :expand-on-click-node="false"
                              @node-click="handleNodeClick"></el-tree>
                </div>
            </div>
            <div class="xt-right">
                <div class="xt-right-head">
                    菜单详情
                </div>
                <!--已选择的数据展示-->
                <div >
                    <div class="xt-right-body">
                        <div class="xt-form-head">
                            菜单详细信息
                        </div>
                        <div class="xt-form" >
                            <div v-if="!formData.id" style="height:35px;line-height:35px;color:gray;text-align:center">请选择菜单</div>
                            <el-form  :model="formData" class="demo-form-inline" v-if="formData.id">
                                <el-row>
                                    <el-col :span="8">
                                         <el-form-item label="所属APP">
                                             <el-tag type="primary">{{formData.appName}}</el-tag>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-form-item label="菜单编码">
                                             <el-tag type="primary">{{formData.code}}</el-tag>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-form-item label="菜单名称">
                                             <el-tag type="primary">{{formData.name}}</el-tag>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="8">
                                        <el-form-item label="菜单链接">
                                             <el-tag type="primary">{{formData.url}}</el-tag>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-form-item label="菜单图标">
                                             <el-tag type="primary"><i :class="formData.icon"></i></el-tag>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </div>
                    </div>
                    <div class="xt-right-list" >
                        <div class="xt-list-head">
                            菜单按钮列表
                        </div>
                        <div class="xt-list-body xt-form">
                            <div v-if="!formData.id" style="height:35px;line-height:35px;color:gray;text-align:center">请选择菜单</div>
                            <el-button size="small" type="primary" @click="handleAdd" v-if="formData.id"><i class="fa fa-plus"></i> 新增</el-button>
                            <el-table :data="formData.roleMenuOperationList" highlight-current-row v-loading="listLoading" style="width: 100%;" v-if="formData.id">
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
                                        <el-button type="danger" :disabled="scope.row.menuId==0" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

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
                <div class="item" v-for="item in allIcon" @click="handleIcon(item)" :key="item">
                    <i :class="item"></i>
                </div>
            </div>
        </el-dialog>
    </section>
</template>
<script>
    import {AppAPI,MenuOperationAPI, getAllIcons,MenuAPI} from '../../api/api'
    import {MessageBox} from '../../common/js/util'

    export default{
        name: 'xt-menu-operator',
        components: {'xt-search':require('../../components/search.vue')},
        data(){
            return {
                defaultProps: {
                    children: 'childs',
                    label: 'name'
                },//属性控件标题以及子列表数据绑定
                filters: {key: '', code: '', name: '', appId:''},
                listLoading: false,
                iconVisible: false,
                addFormVisible: false,
                allIcon: getAllIcons(),
                dataList: [],
                appList:[],
                menuList:[],
                formData:{},
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
            renderContent(h, { node, data, store }) {
                return (
                    <span><span style="color:gray;font-size:12px">[{data.code}]</span>
                    {data.name}
                    </span>
                );
            },
            loadNode(node,resolve){
                this.getMenuList({pageIndex:1,pageSize:2147483647,parentId:node.data.id}).then((menuList) => {
                    node.data.childs=menuList;
                    resolve(menuList);
                })
            },
            handleNodeClick(data){
                this.getDataList(data.id).then((roleMenuOperationList) => {
                    data.roleMenuOperationList=roleMenuOperationList;
                    this.formData=Object.assign({},data);
                })
            },
            handleSearch(){
                this.getAllMenuList()
            },
            handleAddCancel(){
                this.addFormVisible = false;
                this.$refs["addForm"].resetFields();
                this.addForm = {id: 0,appId: '',parentId: 0,url: '',code: '',name: '',icon: '',sort: 0,displayFlag: true}
            },
            handleIcon(icon){
                if (this.editFormVisible) {
                    this.editForm.icon = icon;
                } else {
                    this.addForm.icon = icon;
                }
                this.iconVisible = false;
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
                            _this.getDataList(_this.formData.id).then((roleMenuOperationList) =>{
                                    _this.formData.roleMenuOperationList=roleMenuOperationList;
                                });
                        } else {
                            MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                if (res.data.msg)
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
                this.addForm={id: 0, menuId: this.formData.id, code: '', name: '', icon: ''};
                console.log(this.addForm)
                this.addFormVisible = true;
                //this.$refs["addForm"].resetFields();
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            let para = Object.assign({}, this.addForm);
                            MenuOperationAPI.insertMenuOperation(para).then((res) => {
                                this.addLoading = false;
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible = false;
                                    this.getDataList(this.formData.id).then((roleMenuOperationList) =>{
                                        this.formData.roleMenuOperationList=roleMenuOperationList;
                                    });
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
            //获取菜单按钮列表
            getDataList(menuId) {
                return new Promise((resolve,reject) => {
                    MenuOperationAPI.listMenuAllOperation({menuId: menuId, pageIndex: 1, pageSize: 999999}).then(res => {
                        let roleMenuOperationList=[];
                        if(res.data.code==1) roleMenuOperationList = res.data.data.data;
                        resolve(roleMenuOperationList);
                    });
                })
            },

            //get menu list
            getMenuList(para){
                return new Promise((resolve,reject) => {
                    MenuAPI.listMenu(para).then((res) => {
                        let menuList=[];
                        if(res.data.code==1) menuList=res.data.data.data;
                        menuList.map(function (item) {
                            item.roleMenuOperationList=[];
                            item.childs = [];//声明结构;
                            item.loading = false;
                        });
                        resolve(menuList);
                    });
                })
            },
            getAppList(){
                return new Promise((resolve,reject) => {
                    AppAPI.listApp({pageIndex: 1, pageSize: 9999999}).then((res) => {
                        let appList=[];
                        if(res.data.code==1) appList=res.data.data.data;
                        this.appList = appList;
                        this.filters.appId=appList?appList[0].id:'';
                        resolve(appList);
                    })
                })
            },
            getAllMenuList(){
                let para ={pageIndex:1,pageSize:2147483647};
                para.name = this.filters.name?'%' + this.filters.name + '%':'';
                para.code = this.filters.code?'%' + this.filters.code + '%':'';
                para.appId = this.filters.appId;
                this.getMenuList(para).then((menuList) => {
                    this.menuList = menuList;
                })
            }
        },
        created(){
            this.getAppList().then(() => {
                this.getAllMenuList()
            })
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
    .xt-left {
        display: inline-block;
        float: left;
        width: 20%;
        height: 700px;
        background-color: #EEF1F6;
        border: 1px solid #DFE6EC;
    }
    .xt-left-head {
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        /*color:#ffffff;*/
        text-align: center;
        background-color: #EEF1F6;
    }
    .xt-left-list {
        height: 697px;
        overflow: auto;
    }
    .xt-right {
        display: inline-block;
        width: 78%;
        height: 700px;
        border: 1px solid #E4E8F1;
    }
    .xt-right-head {
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        /*color:#ffffff;*/
        text-align: center;
        background-color: #EEF1F6;
    }
    .xt-right-body {
        /*height: 285px;*/
        /*background-color: pink;*/
        padding: 0.3rem 0.2rem;
    }
    .xt-form-head {
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #DFE6EC;
        background-color: #EEF1F6;
        /*color: #ffffff;*/
    }
    .xt-form {
        padding: 0.3rem 0.4rem;
        border: 1px solid #DFE6EC;
    }
    .xt-right-list {
        padding: .1rem 0.2rem;
        /*height: 300px;*/
        /*background-color: yellow;*/
    }
    .xt-list-head {
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #DFE6EC;
        background-color: #EEF1F6;
        /*color: #ffffff;*/
    }
</style>