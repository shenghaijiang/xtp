# xtp权限系统
## 演示站点
- http://139.196.125.197/xtp
- 用户名:admin 密码:123456
- 清不要删除菜单,数据定时清理

## 实现功能
- 实现角色分配功能
- 实现角色分配功能权限
- 实现角色数据权限设置
- 实现用户单独分配功能
- 实现用户单独分配功能权限
- 实现用户单独数据权限设置
- 数据同步功能，方便系统整合

## 后端部署
- 创建数据库git-xtp
- 导入 xtp.sql 文件
- idea导入项目
- 修改 application.yml 对应的数据库连接,用户名,密码
- 运行项目|java -jar xtp-api.jar --eureka.client.serviceUrl.defaultZone=http://{eureke地址[如果不使用spring cloud方式运行,忽略]}:8761/eureka

## 前端部署
- 确保本地安装node.js v4+
- $ cd my-project
- $ npm install babel-plugin-syntax-jsx babel-plugin-transform-vue-jsx babel-helper-vue-jsx-merge-props babel-preset-es2015 --save-dev
- $ npm --registry https://registry.npm.taobao.org install
- $ npm run dev
- $ 修改src/api/api.js中let base=实际后端java接口地址

## 技术选型
- 核心框架：Spring cloud Dalston.SR3 ; Spring boot 1.5.9 
- 安全框架：Shiro
- 持久层框架：MyBatis
- 数据库：mysql
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.6、Log4j
- 页面交互：Vue2.x

## 授权示例
        @RequiresPermissions({"app:insert"})
        @RequestMapping(value = "insertApp")
        @ResponseBody
        public AjaxResult insertApp() {
            return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
        }
        
- app:对应功能code,insert对应分配的权限code

## 项目截图
- 菜单管理
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/1.png)
- 角色管理
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/2.png)
- 角色数据
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/3.png)
- 角色操作
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/4.png)
- 用户菜单
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/5.png)
- 用户操作
![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/xtp/6.png)

## 后期规划
- 授权查询数据库,大并发下存在性能问题,后期授权整合缓存系统

## 交流、反馈
- 网站：http://www.xtits.cn
- 官方QQ群：**372848506**

![image](http://shenghaijiang-git.oss-cn-shanghai.aliyuncs.com/common/372848506.png)