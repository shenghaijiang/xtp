import axios from 'axios';
import {Message} from 'element-ui';
import VueRouter from 'vue-router'
import routes from '../routes'

axios.defaults.headers = {'Content-Type': 'application/x-www-form-urlencoded'};
/*请求拦截器*/
axios.defaults.transformRequest = [function (data) {
    if (!data)
        data = {};
    if (!data.oauth)
        data.oauth = localStorage.getItem('isautologin') ? (localStorage.getItem("xtit_oauth") || "") : (sessionStorage.getItem("xtit_oauth") || "");
    // Do whatever you want to transform the data
    let ret = ''
    for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
    }
    return ret
}];
let base="http://192.168.2.103:5555/xtp-api"

//系统图标库
export const getAllIcons = () => {
    return [
        'fa fa-address-book',
        'fa fa-address-book-o',
        'fa fa-address-card',
        'fa fa-address-card-o',
        'fa fa-adjust',
        'fa fa-american-sign-language-interpreting',
        'fa fa-anchor',
        'fa fa-archive',
        'fa fa-area-chart',
        'fa fa-arrows',
        'fa fa-arrows-h',
        'fa fa-arrows-v',
        'fa fa-asl-interpreting',
        'fa fa-assistive-listening-systems',
        'fa fa-asterisk',
        'fa fa-at',
        'fa fa-audio-description',
        'fa fa-automobile',
        'fa fa-balance-scale',
        'fa fa-ban',
        'fa fa-bank',
        'fa fa-bar-chart',
        'fa fa-bar-chart-o',
        'fa fa-barcode',
        'fa fa-bars',
        'fa fa-bathtub',
        'fa fa-bath',
        'fa fa-beer',
        'fa fa-tag',
        'fa fa-user-o',
        'fa fa-user-o',
        'fa fa-thermometer-quarter',
        'fa fa-meetup',
        'fa fa-battery-full',
        'fa fa-video-camera',
        'fa fa-university',
        'fa fa-tv',
        'fa fa-tree',
        'fa fa-suitcase',
        'fa fa-trophy',
        'fa fa-umbrella',
        'fa fa-sticky-note',
        'fa fa-wifi',
        'fa fa-user-secret',
        'fa fa-tachometer',
        'fa fa-sliders',
        'fa fa-ship',
        'fa fa-shield',
        'fa fa-shopping-bag',
        'fa fa-server',
        'fa fa-share-alt-square',
        'fa fa-phone-square',
        'fa fa-mortar-board',
        'fa fa-paw',
        'fa fa-object-ungroup',
        'fa fa-map-marker',
        'fa fa-map-signs',
        'fa fa-legal',
        'fa fa-language',
        'fa fa-line-chart',
        'fa fa-industry',
        'fa fa-hotel',
        'fa fa-book',
        'fa fa-bookmark',
        'fa fa-braille',
        'fa fa-bug',
        'fa fa-calculator',
        'fa fa-calendar',
        'fa fa-camera',
        'fa fa-camera-retro',
        'fa fa-cart-plus',
        'fa fa-clock-o',
        'fa fa-coffee',
        'fa fa-cog',
        'fa fa-cogs',
        'fa fa-cloud',
        'fa fa-code-fork',
        'fa fa-comments',
        'fa fa-credit-card',
        'fa fa-database',
        'fa fa-edit',
        'fa fa-envelope',
        'fa fa-external-link',
        'fa fa-eyedropper',
        'fa fa-fax',
        'fa fa-eye',
        'fa fa-flag-checkered',
        'fa fa-fire',
        'fa fa-folder',
        'fa fa-flask',
        'fa fa-gamepad',
        'fa fa-gavel',
        'fa fa-graduation-cap',
        'fa fa-heartbeat',
        'fa fa-hdd-o',
        'fa fa-pie-chart',
        'fa fa-power-off',
        'fa fa-qrcode',
        'fa fa-random',
        'fa fa-road',
        'fa fa-recycle',
        'fa fa-shopping-basket',
        'fa fa-sitemap',
        'fa fa-tags',
        'fa fa-tasks',
        'fa fa-tablet',
        'fa fa-ticket',
        'fa fa-television',
        'fa fa-terminal',
        'fa fa-university',
        'fa fa-users',
        'fa fa-warning',
        'fa fa-cny',
        'fa fa-dollar',
        'fa fa-delicious',
        'fa fa-usb',
        'fa fa-wechat',
        'fa fa-weibo',
        'fa fa-windows',
        'fa fa-hospital-o',
        'fa fa-medkit'
    ];
}
export const requestLogin = params => {
    return axios.post(`${base}/user/loginUser`, params);
};

/*用户相关API*/
export const UserAPI = {
    listUser: (params) => {
        return axios.post(`${base}/user/listUser`, params);
    },
    insertUser: (params) => {
        return axios.post(`${base}/user/insertUser`, {data: JSON.stringify(params)});
    },
    updateUser: (params) => {
        return axios.post(`${base}/user/updateUser`, {data: JSON.stringify(params)});
    },
    deleteUser: (params) => {
        return axios.post(`${base}/user/deleteUser`, params);
    },
    getUser: (params) => {
        return axios.post(`${base}/user/getUser`, params);
    },
    updateUserPassword: (params) => {
        return axios.post(`${base}/user/updateUserPassword`, {data: JSON.stringify(params), password: oldpwd});
    },
    updateUserResetPassword: (params) => {
        return axios.post(`${base}/user/updateUserResetPassword`, params);
    },
}

/*用户菜单按钮相关API*/
export const UserMenuOperationAPI={
    listUserMenuOperation:params=>{
        return axios.post(`${base}/userMenuOperation/listUserMenuOperation`, params);
    },
    updateUserMenuOperation:params=>{
        return axios.post(`${base}/userMenuOperation/updateUserMenuOperation`, params);
    },
    insertUserMenuOperation:params=>{
        return axios.post(`${base}/userMenuOperation/insertUserMenuOperation`, params);
    },
    deleteUserMenuOperation:params=>{
        return axios.post(`${base}/userMenuOperation/deleteUserMenuOperation`, params);
    }
};

/*菜单相关API*/
export const MenuAPI = {
    listMenu: (params) => {
        return axios.post(`${base}/menu/listMenu`, params);
    },
    insertMenu: (params) => {
        return axios.post(`${base}/menu/insertMenu`, {data: JSON.stringify(params)});
    },
    updateMenu: (params) => {
        return axios.post(`${base}/menu/updateMenu`, {data: JSON.stringify(params)});
    },
    deleteMenu: (params) => {
        return axios.post(`${base}/menu/deleteMenu`, params);
    },
    listMenuByUserId: (params) => {
        return axios.post(`${base}/menu/listMenuByUserId`, params);
    },
    listMenuWithOperationByUserId:(params) => {
        return axios.post(`${base}/menu/listMenuWithOperationByUserId.json`, params);
    },
    checkExists:(params)=>{
        return axios.post(`${base}/menu/checkExists`,params);
    }
};

export const UserMenuAPI={
    updateUserMenu: (params) => {
        return axios.post(`${base}/userMenu/updateUserMenu`, params);
    },
    listUserMenu: (params) => {
        return axios.post(`${base}/userMenu/listUserMenu`, params);
    },
}

/*菜单按钮相关API*/
export const MenuOperationAPI={
    insertMenuOperation:(params)=>{
        return axios.post(`${base}/menuOperation/insertMenuOperation`,{data: JSON.stringify(params)});
    },
    deleteMenuOperation:(params)=>{
        return axios.post(`${base}/menuOperation/deleteMenuOperation`, params);
    },
    listMenuOperation:(params)=>{
        return axios.post(`${base}/menuOperation/listMenuOperation`, params);
    },
    listMenuAllOperation:(params)=>{
        return axios.post(`${base}/menuOperation/listMenuAllOperation`, params);//获取一个菜单设置的所有权限
    },
};

/*系统应用相关API*/
export const AppAPI = {
    listApp: params => {
        return axios.post(`${base}/app/listApp`, params);
    },
    insertApp: params => {
        return axios.post(`${base}/app/insertApp`, {data: JSON.stringify(params)});
    },
    updateApp: params => {
        return axios.post(`${base}/app/updateApp`, {data: JSON.stringify(params)});
    },
    deleteApp: params => {
        return axios.post(`${base}/app/deleteApp`, params);
    },
};

/*角色相关API*/
export const RoleAPI = {
    listRole: params => {
        return axios.post(`${base}/role/listRole`, params);
    },
    insertRole: params => {
        return axios.post(`${base}/role/insertRole`, {data: JSON.stringify(params)});
    },
    updateRole: params => {
        return axios.post(`${base}/role/updateRole`, {data: JSON.stringify(params)});
    },
    deleteRole: params => {
        return axios.post(`${base}/role/deleteRole`, params);
    },
};

//用户角色接口定义
export const RoleUserAPI={
    listRoleUser: params => {
        return axios.post(`${base}/roleUser/listRoleUser`, params);
    },
    updateRoleUser: params => {
        return axios.post(`${base}/roleUser/updateRoleUser`, params);
    },
}

//角色菜单接口定义
export const RoleMenuAPI={
    updateRoleMenu: params => {
        return axios.post(`${base}/roleMenu/updateRoleMenu`, params);
    },
    listRoleMenu: params => {
        return axios.post(`${base}/roleMenu/listRoleMenu`, params);
    },
}

/*角色数据权限相关API*/
export const RoleDataRuleAPI={
    listRoleDataRule: params => {
        return axios.post(`${base}/roleDataRule/listRoleDataRule`, params);
    },
    insertRoleDataRule:params => {
        return axios.post(`${base}/roleDataRule/insertRoleDataRule`, {data: JSON.stringify(params)});
    },
    deleteRoleDataRule: params => {
        return axios.post(`${base}/roleDataRule/deleteRoleDataRule`, params);
    },
    updateRoleDataRule: params => {
        return axios.post(`${base}/roleDataRule/updateRoleDataRule`, {data: JSON.stringify(params)});
    }
};

/*角色菜单按钮相关API*/
export const RoleMenuOperationAPI={
    insertRoleMenuOperation:params=>{
        return axios.post(`${base}/roleMenuOperation/insertRoleMenuOperation`, params);
    },
    deleteRoleMenuOperation:params=>{
        return axios.post(`${base}/roleMenuOperation/deleteRoleMenuOperation`, params);
    },
    listRoleMenuOperation:params => {
        return axios.post(`${base}/roleMenuOperation/listRoleMenuOperation`, params);
    }
};

/* 系统配置*/
export const SystemConfigAPI = {
    getSysConfigList(params){
        return axios.post(`${base}/sysConfig/listErrorCode`, params);
    }
}


const router = new VueRouter({
    routes
})
/*响应拦截器*/
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        Message({
            showClose: true,
            message: '与服务器连接错误,请检查服务器并刷新页面',
            type: 'error',
            duration: 0
        });
        return Promise.reject(error.response.data)   // 返回接口返回的错误信息
    });

