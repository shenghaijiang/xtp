<template>
    <el-row class="container">
        <el-col :span="24" class="header">
            <el-col :span="10" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
                {{collapsed ? '' : sysName}}
                <span style="cursor: pointer" @click.prevent="collapse">
                <i class="fa fa-align-justify"></i>
                </span>
            </el-col>
            <el-col :span="14" class="userinfo">
                <el-dropdown trigger="hover" style="float:right">
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
            <aside element-loading-text="菜单加载中" v-loading="menuLoading" :class="collapsed?'xt-menu-collapsed':'xt-menu-expanded'">
        <!--导航菜单-->
        <el-menu :default-active="$route.path" ref="xtMenu" class="el-menu-vertical-demo" @open="handleopen" @close="handleclose" @select="handleselect" unique-opened router v-show="!collapsed" style="overflow-y: auto;-ms-overflow-y: auto;background:#eef1f6;width: 100%">
          <div :key="index" v-for="(item,index) in menuList" v-if="item.displayFlag">
            <el-submenu :index="index+''" v-if="item.childs.length>0">
              <template slot="title">
                <i :class="item.icon"></i>{{item.name}}</template>
              <div :key="childindex" v-for="(child,childindex) in item.childs" v-if="child.displayFlag">
                <el-menu-item :index="child.url" v-if="child.childs.length==0" @click="setCurrentMenu(child,item)">
                  <i :class="child.icon"></i>{{child.name}}</el-menu-item>
                <el-submenu v-else :index="index+'-'+childindex">
                  <template slot="title">
                    <i :class="child.icon"></i>{{child.name}}</template>
                  <el-menu-item :key="index" v-for=" (sub, index) in child.childs" :index="sub.url" v-if="sub.displayFlag" @click="setCurrentMenu(sub,child,item)">
                    <i :class="sub.icon"></i>{{sub.name}}
                  </el-menu-item>
                </el-submenu>
              </div>
            </el-submenu>
            <el-menu-item :index="item.url" style="height: 56px!important;line-height: 56px!important;" v-else @select="setCurrentMenu(item)">
              <i :class="item.icon"></i>{{item.name}}
            </el-menu-item>
          </div>
        </el-menu>
        <!--导航菜单-折叠后-->
        <ul class="el-menu el-menu-vertical-demo collapsed" v-show="collapsed" ref="menuCollapsed">
          <div :key="index" v-for="(item,index) in menuList">
            <li v-if="item.displayFlag" class="el-submenu item">
                <template v-if="item.childs.length==0">
                    <div v-if="item.displayFlag" class="el-submenu__title" style="padding-left: 20px;" @click="setCurrentMenu(item); $router.push({path:item.url,query:{menuId:item.id}})">
                        <i :class="item.icon"></i>
                    </div>
                </template>
              <template v-if="item.childs.length>0">
                <div class="el-submenu__title" style="padding-left: 20px;" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                    <i :class="item.icon"></i>
                </div>
                <div style="border-top: 1px solid #d3d4d6;border-bottom: 1px solid #d3d4d6;border-right: 1px solid #d3d4d6;" :class="'submenu submenu-hook-'+index" @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                  <p style="font-weight: bold;font-size: 16px;color: #20a0ff;border-bottom: 1px solid #d3d4d6;">
                    <span style="display: block;padding-bottom: 5px;border-bottom: solid 3px #20a0ff;text-align: center;padding-right: 10px;padding-left: 10px;width: 100px">{{item.name}}</span>
                  </p>
                  <div style="width:100%;border-bottom:1px solid #d3d4d6;margin-bottom:5px" :key="childindex" v-for="(child,childindex) in item.childs" v-if="child.displayFlag">
                    <div style="display: flex;flex-flow: row;width: 100%;" v-if="child.childs.length>0">
                      <div style="min-width: 120px;font-weight: bold;text-align: right;padding-right:10px;color:#50bfff;font-size: 14px;">{{child.name}}
                        <i v-if="child.childs.length>0">&gt;</i>
                      </div>
                      <div style="flex: 1;display: flex;flex-flow: row wrap;padding-left: 12px">
                        <span :class="$route.path==sub.url?'is-active':''" @click="setCurrentMenu(sub,child,item); $router.push({path:sub.url,query:{menuId:sub.id}})" style="margin-left: 5px;height: 30px;line-height: 30px;cursor: pointer" :key="index" v-for="(sub,subindex) in child.childs" v-if="sub.displayFlag">
                          <i v-if="subindex>0"> |</i>
                          {{sub.name}}
                        </span>
                      </div>
                    </div>
                    <div v-else>
                      <div style="display: flex;flex-flow: row;width: 100%;">
                        <div style="min-width: 120px;font-weight: bold;text-align: right;padding-right:10px;color:#50bfff;font-size: 14px;" :class="$route.path==child.url?'is-active':''" @click="setCurrentMenu(child,item);$router.push({path:child.url,query:{menuId:child.id}})">{{child.name}} </div>
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </li>
            <li v-else class="el-submenu">
              <el-tooltip class="item" effect="dark" :content="item.name" placement="top">
                <div v-if="item.displayFlag" class="el-submenu__title el-menu-item" style="padding-left: 20px;height: 40px;line-height: 40px;padding: 0 20px;" :class="$route.path==item.url?'is-active':''" @click="setCurrentMenu(item);$router.push({path:item.url,query:{menuId:item.id}})">
                  <i :class="item.icon"></i>
                </div>
              </el-tooltip>
            </li>
          </div>
          </li>
        </ul>
      </aside>
<section class="content-container">
    <div class="grid-content bg-purple-light">
          <el-col :span="24" class="breadcrumb-container" v-if="$route.path!='/index'">
            <strong class="title">{{currentMenuInfo.currMenuName}}</strong>
            <el-breadcrumb separator="/" class="breadcrumb-inner">
              <el-breadcrumb-item :key="index" v-for="(item, index) in currentMenuInfo.list">
                {{ item }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </el-col>
          <el-col :span="24" class="content-wrapper">
            <transition name="fade" mode="out-in">
              <router-view></router-view>
            </transition>
          </el-col>
        </div>
    <footer><section>Copyright2017&copy;Version 1.0.0</section></footer>
</section>
</el-col>
</el-row>

</template>

<script>
    import {UserAPI,MenuAPI} from '../api/api';
    export default {
        data() {
            return {
                menuLoading: true,
                sysName: 'xtp权限系统',
                collapsed: false,
                sysUserName: '',
                sysUserAvatar: '',
                allMenuList: [],
                menuList: [],
                currentMenuInfo: { currMenuName: '', list: [] },
                subMenuList: [],//左侧二级菜单
                currentSystem: { name: '', id: 0 }
            }
        },
        methods: {
            setCurrentMenu(currMenu, parentMenu, rootMenu) {
                this.currentMenuInfo = {};
                let tempData = {
                    currMenuName: currMenu.name,
                    list: []
                };
                if (rootMenu) {
                    tempData.list.push(rootMenu.name);
                }
                if (parentMenu) {
                    tempData.list.push(parentMenu.name);
                }
                tempData.list.push(currMenu.name);
                this.currentMenuInfo = tempData;
             },
            handleUserInfo(){
                location.href = '#/userinfo'
            },
            sortMenu(arr) {
                arr.sort(function (a, b) {
                    return a.sort - b.sort;
                })
            },
            loadMenus(){
                let _this = this;
                return new Promise(function (resolve, reject) {
                    MenuAPI.listMenuByUserId({}).then(function (result) {
                        let parentMenus = result.data.data.parentMenu;
                        let allMenus = result.data.data.allMenu;
                        _this.allMenuList = allMenus;
                        sessionStorage.setItem('menu',JSON.stringify(allMenus));
                        parentMenus.map(function (item) {
                            item.childs = [];
                            allMenus.forEach(function (i, index) {
                            if (i.parentId == item.id)
                                item.childs.push(i);
                            });
                            item.childs.sort(function (a, b) {
                            return a.sort - b.sort;
                            })
                            item.childs.map((level1) => {
                            level1.childs = [];
                            allMenus.forEach(function (i, index) {
                                if (i.parentId == level1.id)
                                level1.childs.push(i);
                            });
                            level1.childs.sort(function (a, b) {
                                return a.sort - b.sort;
                            })
                            })
                        });
                        parentMenus.sort(function (a, b) {
                            return a.sort - b.sort;
                        })
                        resolve(parentMenus);
                    }).catch(function (error) {
                        _this.menuLoading = false;
                        // _this.menuList = [];
                        resolve([]);
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
                if(!this.collapsed){
                    this.$refs["xtMenu"].$el.style.width="100%";
                }
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
            this.currentMenuInfo.currMenuName = this.$route.name;
            this.loadMenus().then(function (result) {
            self.menuLoading = false;
            self.menuList = result;
            self.currentSystem = result[0];//默认加载第一个子系统
            self.subMenuList = self.currentSystem.childs;
            });
        },
        beforeUpdate() {
            let _this = this;
            let menuItem = this.allMenuList.find(function (element) {
            return element.url == _this.$route.path;
            })
            if (menuItem) {
            let parentItem = {};
            let list = [];
            _this.menuList.map(function (first) {
                first.childs.map(function (second) {
                if (second.name == menuItem.name) {
                    list.push(first.name, second.name);
                    return true;
                } else {
                    second.childs.map(function (third) {
                    if (third.name == menuItem.name) {
                        list.push(first.name, second.name, third.name);
                        return true;
                    }
                    })
                }
                })
            })
            if (_this.currentMenuInfo.currMenuName != menuItem.name) {
                _this.currentMenuInfo = { currMenuName: menuItem.name, list: list };
            }
            }
        },
        updated() {
        },
        beforeDestroy(){
        },
        destoryed(){
        }
    }

</script>

<style lang="scss" scoped>
@import '~scss_vars';

.item {
  margin-top: 0px;
  margin-right: 0px;
}

.itemText{
    border: 1px solid rgb(32, 160, 255);
    border-radius: 25px;
    line-height: 25px;
    width: 50px;
    text-align: center;
    background-color: #2a68a5;
}
.container {
  position: absolute;
  top: 0px;
  bottom: 0px;
  width: 100%;
  .header {
    height: 45px;
    line-height: 45px;
    background: $color-primary;
    color: #fff;
    .userinfo {
      // text-align: right;
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
      //width:210px;
      height: 45px;
      font-size: 21px;
      padding-left: 10px;
      padding-right: 20px;
      border-color: rgba(238, 241, 146, 0.3);
      border-right-width: 1px;
      border-right-style: solid;
      img {
        width: 30px;
        float: left;
        margin: 10px 10px 10px 18px;
      }
      .txt {
        color: #fff;
      }
    }
    .logo-width {
      width: 210px;
    }
    .logo-collapse-width {
      width: 60px
    }
    .tools {
      padding: 0px 20px;
      /*width: 14px;*/
      height: 45px;
      line-height: 45px;
      cursor: pointer;
      font-size: 1rem;
      i {
        color: #ff4949;
        font-size: 1.5rem;
      }
      .current {
        color: #ff4949;
        font-weight: bold;
      }
    }
  }
  .main {
    display: flex; // background: #324057;
    position: absolute;
    top: 47px;
    bottom: 0px;
    overflow: hidden;
    /*i{*/
    /*font-size: 1.3rem;*/
    /*}*/
    aside {
      flex: 0 0 210px;
      width: 210px;
      /*overflow-y: scroll!important;*/
      .el-menu {
        height: 100%;
      }
      .collapsed {
        width: 60px;
        .item {
          position: relative;
        }
        .submenu {
          width: 600px;
          background-color: #eef1f6;
          position: absolute;
          top: 0px;
          left: 60px;
          z-index: 999;
          height: auto;
          display: none;
        }
      }
    }
    .xt-menu-collapsed {
      flex: 0 0 60px;
      width: 60px;
      background: rgb(238, 241, 246);
    }
    .xt-menu-expanded {
      overflow-y: auto;
      flex: 0 0 210px;
      width: 210px;
      background: rgb(238, 241, 246);
    }
    .content-container {
      // background: #f1f2f7;
      flex: 1; // position: absolute;
      // right: 0px;
      // top: 0px;
      // left: 230px;
      overflow-y: scroll;
      padding: 20px;
      .breadcrumb-container {
        //margin-bottom: 15px;
        .title {
          width: 180px;
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
      footer {
        z-index:-2;
        height: 28px;
        line-height: 28px;
        position: absolute;
        bottom: 0;
        width: 100%;
        background: #eef1f6;
        section {
          width: 230px;
          position: relative;
          margin-left: 50%;
          left: -210px;
          img {
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

.el-menu-item,
.el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}

.mainmenu {
  display: flex;
  flex-flow: row wrap;
  max-width: 210px;
}

.mainmenu span {
  border: solid 1px #e4e4e4;
  height: 30px;
  line-height: 30px;
  width: 100px;
  text-align: center;
  border-radius: 5px;
  margin: 5px;
  cursor: pointer;
  font-size: 14px;
}

.mainmenu span:hover {
  background-color: #C7E6FD;
}

.mainmenu .actived {
  background-color: #20a0ff;
  color: #ffffff;
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