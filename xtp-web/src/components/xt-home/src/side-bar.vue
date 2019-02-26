
<style lang="scss" scoped>
.bar{
    // position: fixed;
    // z-index: 1000px;
    background-color: #1885DD;
    height: 2.8rem;
    line-height: 2.8rem;
    color:#ffffff;
    font-size: 18px;
    border-color: rgba(238, 241, 146, 0.3);
    border-right-width: 1px;
    border-right-style: solid;
    label{
        margin-left: 0.5rem;
    }
}
.sidebar{
    border-color: #eef1f6;
}
.el-menu {
    border-right: solid 1px #eef1f6;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
}
.el-menu--collapse {
    width: 60px;
}
</style>

<template>
    <section >
        <div >
            <el-menu class="el-menu-vertical-demo" :collapse="isCollapse" v-for="(item,index) in menuList" :key="index" v-if="item.displayFlag" :router="true" background-color="rgb(238, 241, 246)" style="position: inherit" >
                <el-submenu :index="item.path"  v-if="item.children.length>=1">
                    <template slot="title" >
                    <i :class="item.icon" ></i>
                    <span slot="title" v-if="!isCollapse">{{item.name}}</span>
                    </template>
                    <div v-for="(child,childIndex) in item.children" :key="childIndex" v-if="child.displayFlag" >
                        <el-submenu :index="child.path"  v-if="child.children.length>=1" style="background-color:white">
                            <template slot="title" >
                                <i :class="item.icon"></i>
                                <span slot="title" >{{child.name}}</span>
                            </template>
                            <div v-for="(grandChild,grandChildIndex) in child.children" :key="grandChildIndex" v-if="grandChild.displayFlag" >
                                <el-submenu :index="grandChild.path"  v-if="grandChild.children.length>=1" style="background-color:white">
                                    <template slot="title" >
                                        <i :class="child.icon"></i>
                                        <span slot="title" >{{grandChild.name}}</span>
                                    </template>
                                </el-submenu>
                                <el-menu-item :index="grandChild.path" :route="grandChild.path" v-else >
                                    <i :class="grandChild.icon"></i>
                                    <span slot="title">{{grandChild.name}}</span>
                                </el-menu-item>
                            </div>
                        </el-submenu>
                        <el-menu-item :index="child.path" :route="child.path" v-else >
                            <i :class="child.icon"></i>
                            <span slot="title">{{child.name}}</span>
                        </el-menu-item>
                    </div>
                </el-submenu>
                <el-menu-item :index="item.path" :route="item.path" v-else >
                    <i :class="item.icon"></i>
                    <span slot="title" >{{item.name}}</span>
                </el-menu-item>
            </el-menu>
        </div>
    </section>
</template>

<script>
import menus from "../menus";
import {MenuAPI} from "../../../modules/xtp";
import { mapGetters } from "vuex";

export default {
    name: "SideBar",
    components: {
    },
    data() {
        return {
            // menuList:[],
            allMenuList: [],
            menuLoading: false
        };
    },
    computed: {
        ...mapGetters(["isCollapse"]),
        menuList () {
            // console.log(this.$store)
            return this.$store.getters.menuList;
        }
	},
    methods: {
        collapse: function () {
            this.$store.dispatch("changeIsCollapse");
        },
        setChildren(item, allMenuList) {
            const childrenList = [];
            allMenuList.map((allItem, allIndex) => {
                allItem.path = allItem.url;
                if (allItem.parentId === item.id) {
                    if (allItem.displayFlag) {childrenList.push(allItem);}
                }
            });
            childrenList.sort(function (a, b) {
                return a.sort - b.sort;
            });
            item.children = childrenList;
            if (item.children.length > 0) {
                item.children.map((childItem, childIndex) => {
                    this.setChildren(childItem, allMenuList);
                });
            }
        },
        loadMenus() {
            return new Promise((resolve, reject) => {
                MenuAPI.listMenuByUserId({}).then(({res}) => {
                    const parentMenus = res.data.data.parentMenu;
                    const allMenus = res.data.data.allMenu;
                    sessionStorage.setItem("menu", JSON.stringify(allMenus));
                    parentMenus.map((item) => {
                        item.path = item.url;
                        this.setChildren(item, allMenus);
                    });
                    parentMenus.sort(function (a, b) {
                        return a.sort - b.sort;
                    });
                    resolve({parentMenus, allMenus});
                })
.catch((error) => {
                    this.menuLoading = false;
                    resolve([], []);
                });
            });
        }
    },
    created() {
        // this.loadMenus().then(({parentMenus,allMenus}) => {
        //     this.menuList = parentMenus;
        //     this.allMenuList = allMenus;
        //     this.$store.dispatch('setMenuList',parentMenus);
        //     this.$store.dispatch('setAllMenuList',this.allMenuList);
        // });
        // this.$store.dispatch('getMenuList',{}).then(({parentMenus,allMenus}) => {
        //     this.menuList = parentMenus;
        //     this.allMenuList = allMenus;
        // })
        // this.menuList=menus;
    }
};
</script>


