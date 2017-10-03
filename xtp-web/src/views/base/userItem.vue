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

        <el-table :data="menuList" @expand="handleExpand" :disabled="!editEnabled">
            <el-table-column type="expand">
                <template scope="props">
                    <div v-if="props.row.checkedSubMenus">
                        <el-table :data="props.row.checkedSubMenus">
                            <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true"></el-table-column>
                            <el-table-column prop="url" label="菜单路径" :show-overflow-tooltip="true"></el-table-column>
                            <el-table-column label="菜单类型">
                                <template scope="menu2">
                                    二级菜单
                                </template>
                            </el-table-column>
                            <el-table-column label="菜单权限" width="300">
                                <template scope="scope">
                                    <el-radio-group v-model="scope.row.type">
                                        <el-radio label="忽略" :disabled="!editEnabled"></el-radio>
                                        <el-radio label="允许" :disabled="!editEnabled"></el-radio>
                                        <el-radio label="禁止" :disabled="!editEnabled"></el-radio>
                                    </el-radio-group>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="url" label="菜单路径" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="菜单类型">
                <template scope="menu">
                    一级菜单
                </template>
            </el-table-column>
            <el-table-column label="菜单权限" width="300">
                <template scope="scope">
                    <el-radio-group v-model="scope.row.type" :disabled="!editEnabled">
                        <el-radio label="忽略" :disabled="!editEnabled"></el-radio>
                        <el-radio label="允许" :disabled="!editEnabled"></el-radio>
                        <el-radio label="禁止" :disabled="!editEnabled"></el-radio>
                    </el-radio-group>
                </template>
            </el-table-column>
        </el-table>
    </section>
</template>
<script>
    import {MenuAPI, RoleAPI, UserAPI,UserMenuAPI,RoleMenuAPI} from '../../api/api';
    export default{
        data() {
            return {
                selectedMenus: [],
                userMenu: [],
                userMenuIds:[],
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
                count: 1,
                radio3: '忽略'
            }
        },
        props: {
            userId: {
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
            getUserMenus(appId){
                let _self = this;
                let para = {pageIndex: 1,pageSize: 999999,userId: _self.userId,appId: _self.appId}
                return new Promise(function (resolve, reject) {
                    UserMenuAPI.listUserMenu({
                        pageIndex: 1,
                        pageSize: 999999,
                        userId: _self.userId
                    }).then(function (res) {
                        _self.userMenu=res.data.data.data;
                    })
                    RoleMenuAPI.listRoleMenu(para).then(function (res) {
                        if (res.data.code === 1) {
                            res.data.data.data.forEach(function (item) {
                                _self.selectedMenus.push(item.menuId);
                            });
                            resolve();
                        }
                    });
                });
            },
            //获取状态
            type2Str(t){
                if (t == -1) {
                    return '忽略'
                } else if (t == 0) {
                    return '禁止'
                } else if (t == 1) {
                    return '允许'
                }
            },
            str2Type(s){
                if (s == '忽略') {
                    return -1
                } else if (s == '禁止') {
                    return 0
                } else if (s == '允许') {
                    return 1
                }
            },
            //获取菜单列表
            getMenus() {
                let _self = this;
                return new Promise(function (resolve, reject) {
                    let para = {pageIndex: 1, pageSize: 999999, appId: _self.appId, userId: _self.userId};
                    MenuAPI.listMenu(para).then((res) => {
                        let menuArr = res.data.data.data;
                        let count = menuArr.length;
                        menuArr.map(function (item) {
                            MenuAPI.listMenu({pageIndex: 1,pageSize: 999999,parentId: item.id,appId: _self.appId}).then(function (res) {
                                item.isIndeterminate = true;
                                item.type = '忽略'
                                item.checkedSubMenus = [];
                                item.childs = res.data.data.data;
                                item.childs.map(function (i) {
                                    i.type = '忽略';
                                    if (_self.selectedMenus.indexOf(i.id) > -1) {
                                        item.checkedSubMenus.push(i)
                                    }
                                    _self.userMenu.map(function(usermenu){
                                        if(usermenu.menuId==i.id){
                                            i.type=_self.type2Str(usermenu.type)
                                        }
                                    })
                                })
                                count--;
                                _self.userMenu.map(function(usermenu){
                                    if(usermenu.menuId==item.id){
                                        item.type=_self.type2Str(usermenu.type)
                                    }
                                })
                                if (item.childs.length == 0) {
                                    if (_self.selectedMenus.indexOf(item.id) > -1) {

                                    }
                                } else if (item.childs.length == item.checkedSubMenus.length) {

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
            handleExpand(row){

            },
            handleSave(){
                let selectArr = [], _self = this, list = [];
                _self.loading.saveLoading = true;
                this.menuList.forEach(function (main) {
                    let mainitem={menuId:main.id,type:_self.str2Type(main.type)}
                    selectArr.push(mainitem);
                    main.childs.forEach(function(item){
                        let data={menuId:item.id,type:_self.str2Type(item.type)};
                        selectArr.push(data);
                    })
                });

                UserMenuAPI.updateUserMenu({data:JSON.stringify(selectArr),userId:_self.userId}).then(function(res){
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
                })
            },
        },
        created() {
            let _self = this;
            this.listLoading = true;
            this.getUserMenus().then(function () {
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
        /*width: 12rem;*/
        border-radius: 5px;
        border: 1px solid #1c8de0;
        box-shadow: 5px 5px 5px #999999;
        margin: .5rem;
        padding: 1rem;
    }

</style>
