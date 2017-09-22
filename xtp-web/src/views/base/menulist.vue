<template>
    <section>
        <!--工具条-->
        <xt-search @click="handleSearch" :addButton="true" @addClick="handleAdd">
            <el-form :inline="true" :model="filters">
                <el-select v-model="filters.selected" placeholder="请选择">
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
        <el-table :data="menuList" highlight-current-row v-loading="listLoading" @expand="expandHandle"
                  style="width: 100%;">
            <!--<el-table-column type="selection" width="55">-->
            <!--</el-table-column>-->
            <el-table-column type="expand">
                <template scope="scope">
                    <el-button size="small" type="warning" @click="handleAdd(scope.row)" icon="plus">新增子菜单</el-button>
                    <el-table
                            :data="scope.row.childs"
                            v-loading="scope.row.loading"
                            style="width: 100%">
                        <el-table-column type="expand">
                            <template scope="props">
                                <xt-menutag :row="props.row" @handleAdd="handleAdd" @handleEdit="handleEdit" @handleDel="handleDel" @reload="callBackFunc"></xt-menutag>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="code"
                                label="菜单编码" minWidth="120">
                            <template scope="scope">
                                <span class="xt-text-hidden" :title="scope.row.code">{{scope.row.code}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="菜单名称" minWidth="120">
                            <template scope="scope">
                                <span class="xt-text-hidden" :title="scope.row.name">{{scope.row.name}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="icon"
                                align="center"
                                label="菜单图标" minWidth="120">
                            <template scope="scope">
                                <i :class="scope.row.icon"></i>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="url"
                                align="center"
                                label="链接地址" minWidth="120">
                            <template scope="scope">
                                <span :title="scope.row.url" class="xt-text-hidden">{{scope.row.url}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="sort"
                                align="center"
                                label="排序" minWidth="120">
                        </el-table-column>
                        <el-table-column
                                prop="displayFlag"
                                align="center"
                                label="是否启用" minWidth="120">
                            <template scope="scope">
                                {{scope.row.displayFlag ? "√" : "×"}}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" minWidth="150">
                            <template scope="scope">
                                <el-button
                                        title="编辑"
                                        type="info"
                                        size="small"
                                        @click="handleEdit(scope.$index, scope.row)" icon="edit"></el-button>
                                <el-button
                                        title="删除"
                                        size="small"
                                        type="danger"
                                        @click="handleDel(scope.$index, scope.row)" icon="delete"></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column
                    prop="appName"
                    align="center"
                    label="所属应用" minWidth="120">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.appName">{{scope.row.appName}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="code"
                    align="center"
                    label="菜单编码" minWidth="120">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.code">{{scope.row.code}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="name"
                    align="center"
                    label="菜单名称" minWidth="120">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.name">{{scope.row.name}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="icon"
                    align="center"
                    label="菜单图标" minWidth="120">
                <template scope="scope">
                    <i :class="scope.row.icon"></i>
                </template>
            </el-table-column>
            <el-table-column
                    prop="url"
                    align="center"
                    label="链接地址" minWidth="120">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.url">{{scope.row.url}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="sort"
                    align="center"
                    label="排序" minWidth="80">
            </el-table-column>
            <el-table-column
                    prop="displayFlag"
                    align="center"
                    label="是否启用" minWidth="120">
                <template scope="scope">
                    {{scope.row.displayFlag ? "√" : "×"}}
                </template>
            </el-table-column>
            <el-table-column label="操作" minWidth="150">
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
                    :page-sizes="[1,10, 20, 30, 40]"
                    :page-size="pageInfo.pageSize"
                    layout="sizes, prev, pager, next"
                    :total="pageInfo.count">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" @close="handleEditCancel">
            <el-form :model="editForm" label-width="100px" :rules="formRules" ref="editForm">
                <el-form-item label="所属应用" prop="appId" v-show="editForm.parentId==0">
                    <el-select v-model="editForm.appId" placeholder="请选择所属应用">
                        <el-option
                                v-for="item in appList"
                                :label="item.name"
                                :value="item.id+''">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜单编码" prop="code">
                    <el-input placeholder="请输入菜单编码" v-model="editForm.code"></el-input>
                </el-form-item>
                <el-form-item label="菜单名称" prop="name">
                    <el-input placeholder="请输入菜单名称" v-model="editForm.name"></el-input>
                </el-form-item>
                <el-form-item label="菜单图标" prop="icon">
                    <span style="color: #1c8de0; font-size: 2rem"><i :class="editForm.icon"></i></span>
                    <el-button type="success" size="mini" @click.native="iconVisible = true">选择图标</el-button>
                </el-form-item>
                <el-form-item label="链接地址" prop="url">
                    <el-input placeholder="请输入链接地址" v-model="editForm.url"></el-input>
                </el-form-item>
                <el-form-item label="排序值" prop="sort">
                    <el-input-number v-model="editForm.sort" placeholder="请输入排序值" :min="0" :controls="false" :max="9999"
                                     style="width: 100%"></el-input-number>
                </el-form-item>
                <el-form-item label="是否启用" prop="displayFlag">
                    <el-checkbox v-model="editForm.displayFlag"></el-checkbox>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="handleEditCancel">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" @close="handleAddCancel">
            <el-form :model="addForm" label-width="100x" :rules="formRules" ref="addForm">
                <el-form-item label="所属应用" prop="appId" v-show="addForm.parentId==0">
                    <el-select v-model="addForm.appId" placeholder="请选择所属应用">
                        <el-option
                                v-for="item in appList"
                                :label="item.name"
                                :value="item.id+''">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="菜单编码" prop="code">
                    <el-input placeholder="请输入菜单编码" v-model="addForm.code"></el-input>
                </el-form-item>
                <el-form-item label="菜单名称" prop="name">
                    <el-input placeholder="请输入菜单名称" v-model="addForm.name"></el-input>
                </el-form-item>
                <el-form-item label="链接地址" prop="url">
                    <el-input placeholder="请输入链接地址" v-model="addForm.url"></el-input>
                </el-form-item>
                <el-form-item label="排序值" prop="sort">
                    <el-input-number v-model="addForm.sort" placeholder="请输入排序值" :min="0" :controls="false" :max="9999"
                                     style="width: 100%"></el-input-number>
                </el-form-item>
                <el-form-item label="菜单图标" prop="icon">
                    <span style="color: #1c8de0; font-size: 1.5rem"><i :class="addForm.icon"></i></span>
                    <el-button type="success" size="mini" @click.native="iconVisible = true">选择图标</el-button>
                    <span style="padding-left: 1rem">是否启用 </span>
                    <el-checkbox v-model="addForm.displayFlag"></el-checkbox>
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
    import {AppAPI, MenuAPI, getAllIcons} from '../../api/api';
    import search from '../../components/search.vue'
    import menuTag from './menuListTag.vue'
    import menuoperator from './menuoperator.vue'
    import {CodeChange, MessageBox, util} from '../../common/js/util'

    export default {
        data() {
            var checkCode = (rule, value, callback) => {
                let param = {code: value};
                if (this.editFormVisible){
                    param={code: value,id:this.editForm.id};
                    this.getHadMenu(param).then(({isExist}) => {
                        if(isExist){
                            return callback(new Error('该菜单编码已存在，请重新输入'));
                        }else{
                            callback();
                        }
                    })
                }else {
                    this.getHadMenu(param).then(({isExist}) => {
                        if(isExist){
                            return callback(new Error('该菜单编码已存在，请重新输入'));
                        }else{
                            callback();
                        }
                    })
                }
            }
            return {
                filters: {
                    key: '', code: '', name: '', selected:''
                },
                pageInfo: {pageIndex: 1, pageSize: 10, count: 0},
                menuList: [],
                appList: [],
                allIcon: [],
                listLoading: false,
                sels: [],//列表选中列
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                iconVisible: false,//选择图标界面
                //编辑界面数据
                editForm: {
                    id: 0,
                    appId: '',
                    parentId: 0,
                    url: '',
                    code: '',
                    name: '',
                    icon: '',
                    sort: 0,
                    displayFlag: true
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                formRules: {
                    url: [
                        {required: true, message: '请输入链接地址', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入菜单编码', trigger: 'blur'},
                        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
                        {validator: checkCode, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入菜单名称', trigger: 'blur'},
                        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
                    ],
                    appId: [
                        {required: true, message: '请选择所属应用', trigger: 'change'},
                    ],
                    icon: [
                        {required: true, message: '请选择菜单图标', trigger: 'change'},
                    ],
                    sort: [{required: false, type: 'number', message: '', trigger: 'change'}]
                },
                //新增界面数据
                addForm: {
                    id: 0,
                    appId: '',
                    parentId: 0,
                    url: '',
                    code: '',
                    name: '',
                    icon: '',
                    sort: 0,
                    displayFlag: true
                },
                callBackFuncs:[]
            }
        },
        components: {'xt-search': search, 'xt-menutag':menuTag},
        methods: {
            callBackFunc(func){
                this.callBackFuncs.push(func);
            },
            handleSearch(){
                this.pageInfo.pageIndex=1;
                this.getMenus();
            },
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
            handleEditCancel(){
                this.editFormVisible = false
                this.$refs["editForm"].resetFields();
                this.editForm = {
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
            handledSizeChange(val){
                this.pageInfo.pageSize = val;
                this.getMenus();
            },
            handleCurrentChange(val){
                this.pageInfo.pageIndex = val;
                this.getMenus();
            },
            //获取菜单列表
            getMenus() {
                let _self = this;
                this.listLoading = true;
                if (this.appList.length == 0) {
                    AppAPI.listApp({pageIndex: 1, pageSize: 9999999}).then(function (result) {
                        _self.listLoading = false;
                        _self.appList = result.data.data.data;
                        let para = Object.assign({}, _self.pageInfo);
                        if (_self.filters.name != '')
                            para.name = '%' + _self.filters.name + '%';
                        para.code = '%' + _self.filters.code + '%';
                        if (_self.filters.selected == ''){
                            _self.filters.selected=_self.appList[0].id;
                            para.appId = _self.filters.selected;
                        }
                        MenuAPI.listMenu(para).then((res) => {
                            _self.pageInfo.pageIndex = res.data.data.currentPage;
                            _self.pageInfo.count = res.data.data.count;
                            let data = res.data.data.data;
                            data.map(function (item) {
                                item.appName = _self.appList.find((i) => i.id === item.appId).name;
                                item.childs = null;//声明结构;
                                item.loading = false;
                            });
                            _self.menuList = data;
                        });
                    }).catch(function (error) {
                        _self.listLoading = false;
                    });
                } else {
                    let para = Object.assign({}, this.pageInfo);
                    para.name = '%' + _self.filters.name + '%';
                    para.code = '%' + _self.filters.code + '%';
                    para.appId = _self.filters.selected;
                    MenuAPI.listMenu(para).then((res) => {
                        _self.pageInfo.pageIndex = res.data.data.currentPage
                        _self.pageInfo.count = res.data.data.count
                        _self.listLoading = false;
                        let data = res.data.data.data;
                        data.map(function (item) {
                            item.appName = _self.appList.find((i) => i.id === item.appId).name;
                            item.childs = null;//声明结构;
                            item.loading = false;
                        });
                        _self.menuList = data;
                    });
                }
            },
            getHadMenu(params){
                return new Promise(function (resolve, reject) {
                    let _this = this,isExist=false;
                    MenuAPI.checkExists(params).then((res) => {
                        isExist=res.data.data;
                        resolve({isExist})
                    })
                })
            },
            //删除
            handleDel: function (index, row) {
                let _self=this;
                let para = {id: row.id};
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(function () {
                    _self.listLoading = true;
                    let para = {id: row.id};
                    MenuAPI.deleteMenu(para).then(function (res) {
                        if (res.data.code == 1) {
                            _self.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                            let menuItemDel=_self.menuList.find(function (menuItem) {
                                return para.id==menuItem.id
                            });
                            if(menuItemDel){
                                _self.getMenus();
                            }else{
                                _self.menuList.map(function (element) {
                                    let childItem=element.childs.find(function (childsItem) {
                                        return childsItem.id==para.id
                                    })
                                    if(childItem){
                                        element.childs.remove(childItem);
                                        return true;
                                    }else{
                                        let childParentItem=element.childs.find(function (childsItem) {
                                            return childsItem.id==row.parentId;
                                        })
                                        if(childParentItem){
                                            _self.callBackFuncs.map((func)=>func('delete',row.parentId));
                                            return true;
                                        }
                                    }
                                })
                            }
                        } else {
                            MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                if (res.data.msg != '' && res.data.msg != 'undefined')
                                    message = res.data.msg;
                                _self.$message({
                                    message: message,
                                    type: 'error'
                                });
                            })
                        }
                        _self.listLoading = false;
                    });
                }).catch(() => {

                });
            },
            //删除
            // handleDel: function (index, row) {
            //     let _this = this;
            //     this.$confirm('确认删除该记录吗?', '提示', {
            //         type: 'warning'
            //     }).then(function () {
            //         _this.listLoading = true;
            //         let para = {id: row.id};
            //         MenuAPI.deleteMenu(para).then(function (res) {
            //             if (res.data.code == 1) {
            //                 _this.$message({
            //                     message: '删除成功',
            //                     type: 'success'
            //                 });
            //                 _this.getMenus();
            //             } else {
            //                 let _this = this;
            //                 MessageBox.codeMessage(res.data.code).then(function ({message}) {
            //                     if (res.data.msg != '' && res.data.msg != 'undefined')
            //                         message = res.data.msg;
            //                     _this.$message({
            //                         message: message,
            //                         type: 'error'
            //                     });
            //                 })
            //             }
            //             _this.listLoading = false;
            //         });
            //     }).catch(() => {

            //     });

            // },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
                this.editForm.appId = this.editForm.appId + '';
            },
            //显示新增界面
            handleAdd: function (obj) {

                if (obj.id) {
                    this.addForm.parentId = obj.id;
                    this.addForm.appId = obj.appId + '';
                } else {
                    this.addForm.parentId = 0;
                    this.addForm.appId = '';
                }
                this.addFormVisible = true;
                //this.$refs["addForm"].resetFields();
            },
            // //编辑
            // editSubmit: function () {
            //     let _this=this;
            //     this.$refs.editForm.validate((valid) => {
            //         if (valid) {
            //             _this.$confirm('确认提交吗？', '提示', {}).then(() => {
            //                 _this.editLoading = true;
            //                 //NProgress.start();
            //                 let para = Object.assign({}, this.editForm);
            //                 delete para.childs;
            //                 MenuAPI.updateMenu(para).then((res) => {
            //                     _this.editLoading = false;
            //                     //NProgress.done();
            //                     if (res.data.code == 1) {
            //                         this.$message({
            //                             message: '提交成功',
            //                             type: 'success'
            //                         });
            //                         this.$refs['editForm'].resetFields();
            //                         this.editFormVisible = false;
            //                         _this.getMenus();
            //                     } else {
            //                         let _this = this;
            //                         MessageBox.codeMessage(res.data.code).then(function ({message}) {
            //                             _this.$message({
            //                                 message: message,
            //                                 type: 'error'
            //                             });
            //                         })
            //                     }
            //                 });
            //             });
            //         }
            //     });
            // },
            //编辑
            editSubmit: function () {
                let _this=this;
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editLoading = true;
                            //NProgress.start();
                            let para = Object.assign({}, this.editForm);
                            delete para.childs;
                            MenuAPI.updateMenu(para).then((res) => {
                                this.editLoading = false;
                                //NProgress.done();
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['editForm'].resetFields();
                                    this.editFormVisible = false;
                                    this.menuList.map(function (element) {
                                        if(para.id==element.id){
                                          _this.copyData(element,para)
                                            return true;
                                        }else{
                                            element.childs.map(function (childItem) {
                                                if(childItem.id==para.id){
                                                    _this.copyData(childItem,para)
                                                    return true;
                                                }else{
                                                    if(childItem.id==para.parentId){
                                                        _this.callBackFuncs.map((func)=>func('edit',para.parentId));
                                                        return true;
                                                    }
                                                }
                                            })
                                        }
                                    })
//                                    this.getMenus();
                                } else {
                                    let _this = this;
                                    MessageBox.codeMessage(res.data.code).then(function ({message}) {
                                        if(res.data.msg) message=res.data.msg
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
            // addSubmit: function () {
            //     let _this = this;
            //     this.$refs.addForm.validate((valid) => {
            //         if (valid) {
            //             _this.$confirm('确认提交吗？', '提示', {}).then(() => {
            //                 _this.addLoading = true;
            //                 //NProgress.start();
            //                 let para = Object.assign({}, this.addForm);
            //                 delete para.childs;
            //                 MenuAPI.insertMenu(para).then((res) => {
            //                     _this.addLoading = false;
            //                     //NProgress.done();
            //                     if (res.data.code == 1) {
            //                         _this.$message({
            //                             message: '提交成功',
            //                             type: 'success'
            //                         });
            //                         _this.$refs['addForm'].resetFields();
            //                         _this.addFormVisible = false;
            //                         _this.getMenus();
            //                     } else {
            //                         let _this = this;
            //                         MessageBox.codeMessage(res.data.code).then(function ({message}) {
            //                             if (res.data.msg != '' && res.data.msg != 'undefined')
            //                                 message = res.data.msg;
            //                             _this.$message({
            //                                 message: message,
            //                                 type: 'error'
            //                             });
            //                         })
            //                     }
            //                 });
            //             });
            //         }
            //     });
            // },
            addSubmit: function () {
                let _this=this;
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.addLoading = true;
                            let para = Object.assign({}, this.addForm);
                            delete para.childs;
                            MenuAPI.insertMenu(para).then((res) => {
                                this.addLoading = false;
                                if (res.data.code == 1) {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible = false;
                                    para.id=res.data.data.id;
                                    if(para.parentId==0){
                                        _this.getMenus();
                                        return true;
                                    }else{
                                        this.menuList.map(function (element) {
                                            if(!element.childs){
                                                element.childs=[];
                                            }
                                            if(element.id==para.parentId){
                                                element.childs.push(JSON.parse(JSON.stringify(para)))
                                                return true;
                                            }else{
                                                element.childs.map(function (childsItem) {
                                                    if(childsItem.id==para.parentId){
                                                        _this.callBackFuncs.map((func)=>func('add',para.parentId));
                                                        return true;
                                                    }
                                                })
                                            }
                                        })
                                    }
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
                            });
                        });
                    }
                });
            },
            expandHandle(row,expanded){
                return new Promise(function (resolve, reject) {
                    if(expanded){
//                        if (row.childs == null) {
                            row.loading = true;
                            MenuAPI.listMenu({pageIndex: 1, pageSize: 999999, parentId: row.id}).then(function (res) {
                                row.loading = false;
                                let childList=res.data.data.data;
                                childList.map(function (element) {
                                    element.childs=[];
                                })
                                childList.sort(function (a,b) {
                                    return a.sort-b.sort;
                                })
                                row.childs = childList;
                                resolve()
                            })
                       /* } else {
                            resolve()
                        }*/
                    }
                })
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
                    MenuAPI.deleteMenu(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        _this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        _this.getMenus();
                    });
                }).catch(() => {

                });
            },
        },

        mounted() {
            this.getMenus();
            this.allIcon = getAllIcons()
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