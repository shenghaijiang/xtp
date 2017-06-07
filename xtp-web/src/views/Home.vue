<template>
    <el-row class="container">
        <el-col :span="24" class="header">
            <el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
                {{collapsed ? '' : sysName}}
            </el-col>
            <el-col :span="10">
                <div class="tools" @click.prevent="collapse">
                    <i class="fa fa-align-justify"></i>
                </div>
            </el-col>
            <el-col :span="4" class="userinfo">
                <el-dropdown trigger="hover">
                    <span class="el-dropdown-link userinfo-inner"><img style="background-color: #eeeeee"
                                                                       src="../assets/user.png"/> {{sysUserName}}</span>
                    <el-dropdown-menu slot="dropdown">
                        <!--<el-dropdown-item>我的消息</el-dropdown-item>-->
                        <el-dropdown-item @click.native="handleUserInfo"><i class="fa fa-drivers-license-o"></i> 我的资料
                        </el-dropdown-item>
                        <el-dropdown-item divided @click.native="logout"><i class="fa fa-sign-out"></i> 退出登录
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-col>
        </el-col>
        <el-col :span="24" class="main">
            <aside element-loading-text="菜单加载中" v-loading="menuLoading"
                   :class="collapsed?'menu-collapsed':'menu-expanded'">
                <!--导航菜单-->
                <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleopen"
                         @close="handleclose" @select="handleselect"
                         unique-opened router v-show="!collapsed">
                    <template v-for="(item,index) in menuList" v-if="item.displayFlag">
                        <el-submenu :index="index+''" v-if="item.childs.length>0">
                            <template slot="title"><i :class="item.icon"></i>{{item.name}}</template>
                            <div class="xtscroll" :style="{maxHeight:(26-(index*3))+'rem'}">
                                <el-menu-item v-for="child in item.childs" :index="child.url" v-if="child.displayFlag">
                                    <i :class="child.icon"></i>{{child.name}}
                                </el-menu-item>
                            </div>
                        </el-submenu>
                        <el-menu-item :index="item.url" style="height: 56px!important;line-height: 56px!important;"
                                      v-else><i :class="item.icon"></i>{{item.name}}
                        </el-menu-item>
                    </template>
                </el-menu>
                <!--导航菜单-折叠后-->
                <ul class="el-menu el-menu-vertical-demo collapsed" v-show="collapsed" ref="menuCollapsed">
                    <li v-for="(item,index) in menuList" v-if="item.displayFlag" class="el-submenu item">
                        <template v-if="item.childs.length>0">
                            <div class="el-submenu__title" style="padding-left: 20px;" @mouseover="showMenu(index,true)"
                                 @mouseout="showMenu(index,false)"><i :class="item.icon"></i></div>
                            <ul class="xtscroll el-menu submenu" :class="'submenu-hook-'+index"
                                @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                                <li v-for="child in item.childs" v-if="child.displayFlag" class="el-menu-item"
                                    style="padding-left: 40px;" :class="$route.path==child.url?'is-active':''"
                                    @click="$router.push(child.url)">{{child.name}}
                                </li>
                            </ul>
                        </template>
                        <template v-else>
                    <li class="el-submenu">
                        <div class="el-submenu__title el-menu-item"
                             style="padding-left: 20px;height: 40px;line-height: 40px;padding: 0 20px;"
                             :class="$route.path==item.url?'is-active':''"
                             @click="$router.push(item.url)"><i :class="item.icon"></i>
                        </div>
                    </li>
</template>
</li>
</ul>
</aside>
<section class="content-container">
    <div class="grid-content bg-purple-light">
        <el-col :span="24" class="breadcrumb-container" v-if="$route.path!='/index'">
            <strong class="title">{{$route.name}}</strong>
            <el-breadcrumb separator="/" class="breadcrumb-inner">
                <el-breadcrumb-item v-for="item in $route.matched">
                    {{ item.name }}
                </el-breadcrumb-item>
            </el-breadcrumb>
        </el-col>
        <el-col :span="24" class="content-wrapper">
            <transition name="fade" mode="out-in">
                <router-view></router-view>
            </transition>
        </el-col>
    </div>
    <footer><section><span>Copyright</span><img src="../assets/copyright.png" alt=""><span>2017  Version 1.0.0</span></section></footer>
</section>
</el-col>
</el-row>

</template>

<script>
    import {UserAPI} from '../api/api';
    export default {
        data() {
            return {
                menuLoading: true,
                sysName: 'xtp权限系统',
                collapsed: false,
                sysUserName: '',
                sysUserAvatar: '',
                menuList: []
            }
        },
        methods: {
            handleUserInfo(){
                location.href = '#/userinfo'
            },
            loadMenus(){
                let _this = this;
                return new Promise(function (resolve, reject) {
                    UserAPI.getUserMenuList({}).then(function (result) {
                        let parentMenus = result.data.data.parentMenu;
                        let allMenus = result.data.data.allMenu;
                        parentMenus.map(function (item) {
                            item.childs = [];
                            allMenus.forEach(function (i, index) {
                                if (i.parentId == item.id)
                                    item.childs.push(i);
                            });
                        });
                        resolve(parentMenus);
                    }).catch(function (error) {
                        _this.menuLoading = false;
                        _this.menuList = [];
                    });
                })

            },
            onSubmit() {
                console.log('submit!');
            },
            handleopen() {
                //console.log('handleopen');
            },
            handleclose() {
                //console.log('handleclose');
            },
            handleselect: function (a, b) {

            },
            //退出登录
            logout: function () {
                var _this = this;
                this.$confirm('确认退出吗?', '提示', {
                    //type: 'warning'
                }).then(() => {
                    localStorage.setItem("Logout", new Date());
                    _this.$router.push('/login');
                }).catch(() => {

                });
            },
            //折叠导航栏
            collapse: function () {
                this.collapsed = !this.collapsed;
            },
            showMenu(i, status){
                this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
            }
        },
        mounted() {
            var user = localStorage.getItem('user');
            if (user) {
                this.sysUserName = user;
                //user = JSON.parse(user);
                //this.sysUserName = user.name || '';
                //this.sysUserAvatar = user.avatar || '';
            }

        },
        created(){
            let self = this;
            this.loadMenus().then(function (result) {
                self.menuLoading = false;
                self.menuList = result;
            });
        }
    }

</script>

<style lang="scss" scoped>
    @import '~scss_vars';

    .container {
        position: absolute;
        top: 0px;
        bottom:0px;
        width: 100%;
        .header {
            height: 45px;
            line-height: 45px;
            background: $color-primary;
            color: #fff;
            .userinfo {
                text-align: right;
                padding-right: 35px;
                float: right;
                .userinfo-inner {
                    cursor: pointer;
                    color: #fff;
                    img {
                        width: 40px;
                        height: 40px;
                        border-radius: 20px;
                        margin: 3px 0px 0px 10px;
                        float: right;
                    }
                }
            }
            .logo {
                //width:230px;
                height: 45px;
                font-size: 22px;
                padding-left: 20px;
                padding-right: 20px;
                border-color: rgba(238, 241, 146, 0.3);
                border-right-width: 1px;
                border-right-style: solid;
                img {
                    width: 40px;
                    float: left;
                    margin: 10px 10px 10px 18px;
                }
                .txt {
                    color: #fff;
                }
            }
            .logo-width {
                width: 230px;
            }
            .logo-collapse-width {
                width: 60px
            }
            .tools {
                padding: 0px 23px;
                width: 14px;
                height: 45px;
                line-height: 45px;
                cursor: pointer;
            }
        }
        .main {
            display: flex;
            // background: #324057;
            position: absolute;
            top: 47px;
            bottom: 0px;
            overflow: hidden;
            /*i{*/
            /*font-size: 1.3rem;*/
            /*}*/
            aside {
                flex: 0 0 230px;
                width: 230px;
                overflow-y: scroll!important;
                .el-menu {
                    height: 100%;
                }
                .collapsed {
                    width: 60px;
                    .item {
                        position: relative;
                    }
                    .submenu {
                        position: absolute;
                        top: 0px;
                        left: 60px;
                        z-index: 99999;
                        height: auto;
                        display: none;
                    }

                }
            }
            .menu-collapsed {
                flex: 0 0 60px;
                width: 60px;
            }
            .menu-expanded {
                flex: 0 0 230px;
                width: 230px;
            }
            .content-container {
                // background: #f1f2f7;
                flex: 1;
                // position: absolute;
                // right: 0px;
                // top: 0px;
                // left: 230px;
                overflow-y: scroll;
                padding: 20px;
                .breadcrumb-container {
                    //margin-bottom: 15px;
                    .title {
                        width: 200px;
                        float: left;
                        color: #475669;
                    }
                    .breadcrumb-inner {
                        float: right;
                    }
                }
                .content-wrapper {
                    background-color: #fff;
                    box-sizing: border-box;
                    margin-bottom: 20px;
                }
                footer{
                    height: 28px;
                    line-height: 28px;
                    position: absolute;
                    bottom: 0;
                    width: 100%;
                    background:#eef1f6;
                    section{
                        width: 220px;
                        position: relative;
                        margin-left: 50%;
                        left: -210px;
                        img{
                            display: inline;
                            line-height: 28px;
                            vertical-align: bottom;
                        }
                    }
                }
            }
        }
    }

    .xtscroll {
        overflow-y: auto;
        overflow-x: hidden;
        max-height: 25rem;
    }

    .xtscroll::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
        background-color: #ffffff;
    }

    .xtscroll::-webkit-scrollbar {
        width: 8px;
        background-color: #ffffff;
    }

    .xtscroll::-webkit-scrollbar-thumb {
        background-color: #0082e6;
        border: 2px solid #afddff;
        border-radius: 5px;
    }

    .el-menu-item, .el-submenu__title {
        height: 40px !important;
        line-height: 40px !important;
    }

    /*.el-table td*/
    /*{*/
    /*height: 25px !important;*/
    /*}*/
    /*.el-table th*/
    /*{*/
    /*height: 30px !important;*/
    /*}*/
    /*.el-table .cell{*/
    /*line-height: 18px !important;*/
    /*}*/
    /*.el-table__expand-icon*/
    /*{*/
    /*height: 18px !important;*/
    /*}*/
    /*.toolbar*/
    /*{*/
    /*height: 45px;*/
    /*padding: 5px 0px 5px 10px!important;*/
    /*}*/
    /*.el-button--small{*/
    /*padding: 3px 4px !important;*/
    /*}*/
    /*.el-table .cell, .el-table th>div{*/
    /*padding-left: 5px !important;*/
    /*padding-right: 5px !important;*/
    /*}*/
    /*.el-table__expanded-cell{*/
    /*padding: 5px 10px !important;*/
    /*}*/
    /*.el-card__body*/
    /*{*/
    /*padding: 10px!important;*/
    /*}*/
</style>