<template>
    <div>
        <el-button size="small" type="warning" @click="handleAdd(row)" icon="plus">新增子菜单</el-button>
        <el-table
                :data="menuList"
                v-loading="loading"
                style="width: 100%">
            <el-table-column
                    prop="code"
                    label="菜单编码" minWidth="115">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.code">{{scope.row.code}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="菜单名称" minWidth="115">
                <template scope="scope">
                    <span class="xt-text-hidden" :title="scope.row.name">{{scope.row.name}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="icon"
                    align="center"
                    label="菜单图标" minWidth="115">
                <template scope="scope">
                    <i :class="scope.row.icon"></i>
                </template>
            </el-table-column>
            <el-table-column
                    prop="url"
                    align="center"
                    label="链接地址" minWidth="115">
                <template scope="scope">
                    <span :title="scope.row.url" class="xt-text-hidden">{{scope.row.url}}</span>
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
                    label="是否启用">
                <template scope="scope">
                    {{scope.row.displayFlag ? "√" : "×"}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" minWidth="120">
                <template scope="scope">
                    <el-button
                            title="编辑"
                            type="success"
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
    </div>
</template>
<script>
    import {AppAPI, MenuAPI, getAllIcons} from '../../api/api';
    export default {
        data(){
            return {
                menuList: [],
                loading: false,
            }
        },
        props: {
            row:{
                type:Object/Array,
                default:{}
            }
        },
        methods:{
            handleEdit(index,row){
                this.$emit('handleEdit',index,row);
                console.log(row);
            },
            handleDel(index,row){
                this.$emit('handleDel',index,row);
                console.log(row);
            },
            handleAdd(row){
                this.$emit('handleAdd',row);
                console.log(row);
            },
            reload(status,id){
                let reload=false;
                let _this=this;
                if (this.row.id != 0) {
                    if(status=='first') reload=true;
                    if(status=='delete'||status=='edit'||status=='add'){
                        if(_this.row.id==id){
                            reload=true
                        }else{
                            reload=false
                        }
                    }
                    if(status){
                        this.loading = true;
                        MenuAPI.listMenu({pageIndex: 1, pageSize: 999999, parentId: this.row.id}).then(res => {
                                this.loading = false;
                            if(res.data.code==1){
                                let list=res.data.data.data;
                                list.sort(function (a,b) {
                                    return a.sort-b.sort;
                                })
                                this.menuList=list;
                            }
                        })
                    }
                }
            }
        },
        created(){
            this.reload('first');
            this.$emit('reload',this.reload);
        },
        mounted(){
        }
    }
</script>
