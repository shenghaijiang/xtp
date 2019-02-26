import Model from "./model.vue";
import Role from "./role/role-list";
import UserInfo from "./user-info/use-info";
import App from "./app/app";
import Menu from "./menu/menu";
import User from "./user/user";
import UserMenu from "./user-menu/user-menu";
import UserMenuOperation from "./user-menu-operation/user-menu-operation";
import UserButtonRule from "./user-button-rule/user-button-rule";
import Structure from "./structure/structure";
import RoleDataRule from "./role-data-rule/role-data-rule";

export const components = [
  // {path: "/:modelName/:childModelName/:pageName", title: "模板", name: "model", component: Model},
  // {path: "/:modelName/:pageName", title: "模板", name: "model1", component: Model},
  // {path: "/:modelName", title: "模板", name: "model2", component: Model},
  {path: "/model/:modelName/:childModelName/:pageName", title: "模板", name: "model3", component: Model},
  {path: "/model/:modelName/:pageName", title: "模板", name: "model4", component: Model},
  {path: "/model/:modelName", title: "模板", name: "model5", component: Model},
  {path: "/role", title: "角色管理", name: "Role", component: Role},
  {path: "/userinfo", title: "个人信息设置", name: "UserInfo", component: UserInfo},
  {path: "/app", title: "应用管理", name: "App", component: App},
  {path: "/menu", title: "功能管理", name: "Menu", component: Menu},
  {path: "/user", title: "用户管理", name: "User", component: User},
  {path: "/usermenu2", title: "用户功能管理", name: "UserMenu", component: UserMenu},
  {path: "/newmenuoperation", title: "功能权限", name: "UserMenuOperation", component: UserMenuOperation},
  {path: "/userbuttonrule", title: "功能权限(用户)", name: "UserButtonRule", component: UserButtonRule},
  {path: "/roledatarule", title: "功能权限(角色)", name: "RoleDataRule", component: RoleDataRule},
  {path: "/structure", title: "组织架构", name: "Structure", component: Structure}
];

export const baseUrl = "/";
