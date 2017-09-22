<template>
    <section v-loading="listLoading">
        <!--工具条-->
        <el-col :span="24" style="padding-bottom: 8px;border-bottom: 1px solid #eeeeee">
            <el-button-group>
                <el-button type="primary" :disabled="editEnabled" @click="handleEdit" icon="edit"
                           title="编辑"></el-button>
                <el-button type="warning" :disabled="!editEnabled" @click="handleCancel" title="取消"><i
                        class="fa fa-mail-reply"></i></el-button>
                <el-button type="success" :disabled="!editEnabled" @click="handleSave" title="保存"
                           :loading="loading.saveLoading"><i class="fa fa-save"></i></el-button>
            </el-button-group>
        </el-col>
        <div v-for="item in menuList">
            <fieldset v-if="item.childs.length>0" class="box" :disabled="!editEnabled">
                <legend>
                    <el-checkbox :disabled="!editEnabled" v-model="item.checkAll" @change="handleCheckAllChange(item)">
                        {{item.name}}
                    </el-checkbox>
                </legend>
                <div style="">
                    <div v-for="subitem in item.childs">
                        <!--<el-tree :data="item.childs"-->
                        <!--:props="props"-->
                        <!--show-checkbox-->
                        <!--@check-change="handleCheckChange" style="margin: .5rem;"-->
                        <!--:default-checked-keys="selectedMenus"-->
                        <!--node-key="id"-->
                        <!--:ref="'tree'+item.id"-->
                        <!--&gt;-->
                        <!--</el-tree>-->
                        <el-checkbox :disabled="!editEnabled" v-model="subitem.checked" style="margin-bottom: 5px;">{{subitem.name}}</el-checkbox>
                    </div>
                </div>
            </fieldset>
        </div>
        <fieldset class="box">
            <legend>
                单级菜单
            </legend>
            <div v-for="item in menuList">
                <el-checkbox v-if="item.childs.length==0" :disabled="!editEnabled" v-model="item.checkAll"
                             @change="handleCheckedChange(item)" style="margin-bottom: 5px;">
                    {{item.name}}
                </el-checkbox>
            </div>
        </fieldset>
    </section>
</template>
<script>
    import {MenuAPI, RoleAPI, MenuOperatorAPI, RoleMenuOperatorAPI,RoleMenuAPI} from '../../api/api';
    export default{
        data() {
            return {
                selectedMenus: [],
                editEnabled: false,
                menuList: [],
                listLoading: true,
                loading: {saveLoading: false},
                regions: [{
                    'name': '基本信息'
                }, {
                    'name': '基本信息基本信息基本信息'
                }],
                props: {
                    label: 'name',
                    children: 'childs'
                },
                count: 1
            }
        },
        props: {
            roleId: {
                type: Number,
                default: 0
            },
            appId: {
                type: Number,
                default: 0
            }
        },
        methods: {
            handleEdit(){
                this.editEnabled = true;
            },
            handleCancel(){
                this.editEnabled = false;
            },
            getRoleMenus(appId){
                let _self = this;
                let para = {
                    pageIndex: 1,
                    pageSize: 999999,
                    roleId: _self.roleId,
                    appId: _self.appId
                }
                if (appId) {

                }
                return new Promise(function (resolve, reject) {
                    RoleMenuAPI.listRoleMenu(para).then(function (res) {
                        if (res.data.code === 1) {
                            res.data.data.data.forEach(function (item) {
                                _self.selectedMenus.push(item.menuId);
                            });
                        }
                    });
                    resolve();
                });
            },
            //获取菜单列表
            getMenus() {
                let _self = this;
                return new Promise(function (resolve, reject) {
                    let para = {pageIndex: 1, pageSize: 999999, appId: _self.appId};
                    MenuAPI.listMenu(para).then((res) => {
                        let menuArr = res.data.data.data;
                        let count = menuArr.length;
                        menuArr.map(function (item) {
                            MenuAPI.listMenu({
                                pageIndex: 1,
                                pageSize: 999999,
                                parentId: item.id,
                                appId: _self.appId
                            }).then(function (res) {
                                item.isIndeterminate = true;
                                item.checkAll = false;
                                item.checkedSubMenus = [];
                                item.childs = res.data.data.data;
                                item.childs.map(function (i) {
                                    i.checked = false;
                                    if (_self.selectedMenus.indexOf(i.id) > -1) {
                                        i.checked = true;
                                        item.checkedSubMenus.push(i)
                                    }
                                })
                                count--;
                                if (item.childs.length == 0) {
                                    if (_self.selectedMenus.indexOf(item.id) > -1)
                                        item.checkAll = true;
                                } else if (item.childs.length == item.checkedSubMenus.length) {
                                    item.checkAll = true;
                                }
                                if (count == 0)
                                    resolve(menuArr);
                            })
                        });
                    });
                });
            },
            handleCheckAllChange(item){
                item.childs.map(function (i) {
                    i.checked = item.checkAll;
                });
            },
            handleCheckedChange(item){

            },
            handleSave(){
                let selectArr = [], _self = this, list = [];
                this.menuList.forEach(function (main) {
                    if (main.checkAll) {
                        list.push(main.id);
                    }
                    main.childs.forEach(function (sub) {
                        if (sub.checked) {
                            selectArr.push(sub.id);
                            if (selectArr.indexOf(sub.parentId) < 0) {
                                selectArr.push(sub.parentId);
                            }
                        }
                    })
                })

                list.map(function (item) {
                    selectArr.push(item);
                })
                _self.loading.saveLoading = true;

                RoleMenuAPI.updateRoleMenu({
                    roleId: _self.roleId,
                    appId: _self.appId,
                    menuIds: selectArr.join(',')
                }).then(function (res) {
                    _self.loading.saveLoading = false;
                    if (res.data.code == 1) {
                        _self.editEnabled = false;
                        _self.listLoading = false;
                        _self.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                    } else {
                        _self.$message({
                            message: "保存失败，请重试！",
                            type: 'error'
                        });
                    }
                });
            },
        },
        created() {
            let _self = this;
            this.listLoading = true;
            this.getRoleMenus().then(function () {
                _self.getMenus().then(function (obj) {
                    _self.listLoading = false;
                    _self.menuList = obj;
                });
            });
        }
    }
</script>
<style scoped>
    section {
        display: flex;
        flex-flow: row wrap;
    }

    section .box {
        width: 12rem;
        border-radius: 5px;
        border: 1px solid #1c8de0;
        box-shadow: 5px 5px 5px #999999;
        margin: .5rem;
        padding: 1rem;
    }

</style>