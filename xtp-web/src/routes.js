(function(){
    if(!sessionStorage.length) {
        // 这个调用能触发目标事件，从而达到共享数据的目的
        localStorage.setItem('getSessionStorage',Date.now());
    };

    // 该事件是核心
    window.addEventListener('storage',function(event){
        if(event.key =='getSessionStorage') {
            // 已存在的标签页会收到这个事件
            localStorage.setItem('sessionStorage',JSON.stringify(sessionStorage));
            localStorage.removeItem('sessionStorage');
        } else if(event.key =='sessionStorage'&& !sessionStorage.length){
            // 新开启的标签页会收到这个事件
            var data =JSON.parse(event.newValue);
            for(var key in data) {
                sessionStorage.setItem(key, data[key]);
            }
        }else if(event.key=="Logout"){
            //所有已开启的标签页都会收到退出登录的请求
            sessionStorage.clear();
            localStorage.clear();
            location.href="/";
        }
    });
})();

const home = r => require.ensure([], () => r(require('./views/Home.vue')), 'home')
const login = r => require.ensure([], () => r(require('./views/Login.vue')), 'login')
const notfound = r => require.ensure([], () => r(require('./views/404.vue')), 'notfound')
const userinfo = r => require.ensure([], () => r(require('./views/base/userinfo')), 'userinfo')
const menu = r => require.ensure([], () => r(require('./views/base/menulist')), 'menu')
const userlist = r => require.ensure([], () => r(require('./views/base/userlist')), 'userlist')
const role = r => require.ensure([], () => r(require('./views/base/rolelist')), 'role')
const menuoperator = r => require.ensure([], () => r(require('./views/base/menuoperator')), 'menuoperator')
const roledatarule = r => require.ensure([], () => r(require('./views/base/roledatarule')), 'roledatarule')
const usermenu = r => require.ensure([], () => r(require('./views/base/usermenu')), 'usermenu')
const userbuttonrule = r => require.ensure([], () => r(require('./views/base/userbuttonrule')), 'userbuttonrule')
const newmenuoperation = r => require.ensure([],() => r(require('./views/base/newmenuoperation')),'newmenuoperation')

let routes = [
    {
        path: '/login',
        component: login,
        name: '',
        hidden: true
    },
    // {
    //     path: '/404',
    //     component: notfound,
    //     name: '',
    //     hidden: true
    // },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: home,
        name: '基础信息',
        iconCls: 'fa fa-bank',//图标样式class
        children: [
            {path: '/404', component: notfound, name: '', hidden: true},
            { path: '/userinfo', component: userinfo, name: '个人信息设置' },
            { path: '/app', component:require('./views/base/applist'),name:'应用管理' },
            { path: '/menu', component:menu,name:'菜单管理' },
            { path: '/user', component: userlist, name: '用户管理' },
            { path: '/userbuttonrule', component: userbuttonrule, name: '用户按钮管理' },
            { path: '/role', component: role, name: '角色管理' },
            { path: '/menuoperator', component: menuoperator, name: '菜单按钮管理' },
            {path:'/roledatarule',component:roledatarule,name:'角色数据/按钮权限设置'},
            {path:'/usermenu2',component:usermenu,name:'用户菜单'},
            {path:'/newmenuoperation',component:newmenuoperation,name:'新菜单按钮管理'}
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;