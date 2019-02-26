package cn.xtits.xtp.shiro;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.JwtUtil;
import cn.xtits.xtp.dto.MenuWithOperationDto;
import cn.xtits.xtp.dto.UserMenuWithOperationDto;
import cn.xtits.xtp.entity.MenuOperation;
import cn.xtits.xtp.entity.RoleUser;
import cn.xtits.xtp.entity.RoleUserExample;
import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.interceptor.LoginToken;
import cn.xtits.xtp.service.MenuService;
import cn.xtits.xtp.service.RoleUserService;
import cn.xtits.xtp.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

    @Autowired
    private StringRedisTemplate template;

    @Resource
    private MenuService menuService;

    @Resource
    private UserService userService;

    @Resource
    private RoleUserService roleUserService;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        {
            Claims claims = null;
            try {
                claims = JwtUtil.parseJWT(principals.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginToken t = JsonUtil.fromJson(claims.getSubject(), LoginToken.class);
            int userId = t.getUserId();
            RoleUserExample roleUserExample = new RoleUserExample();
            RoleUserExample.Criteria criteria = roleUserExample.createCriteria();
            criteria.andUserIdEqualTo(userId);


            List<RoleUser> roleUserList = roleUserService.listByExample(roleUserExample);

            String role = "";
            for (RoleUser roleUser : roleUserList) {
                role += roleUser.getRoleId() + ",";
            }


            UserMenuWithOperationDto menuList = menuService.listMenuWithOperationByUserId(userId);

            Set<String> permission = new HashSet<>();

            for (MenuWithOperationDto item : menuList.getAllMenu()) {
                String menu = "";
                List<MenuOperation> operationList = item.getOperationList();
                menu += item.getCode() + ":";
                if(operationList.size()>0) {
                    for (MenuOperation menuOperation : operationList) {
                        menu += menuOperation.getCode() + ",";
                    }
                    menu = menu.substring(0, menu.length() - 1);
                    permission.add(menu);
                }
            }

            User user = userService.getByPrimaryKey(userId);
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addRole(role.substring(0, role.length() - 1));
            simpleAuthorizationInfo.addStringPermissions(permission);
            return simpleAuthorizationInfo;
        }
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
//        String username = JWTUtil.getUsername(token);
//        if (username == null) {
//            throw new AuthenticationException("token invalid");
//        }
//
//        UserBean userBean = service.getUser(username);
//        if (userBean == null) {
//            throw new AuthenticationException("User didn't existed!");
//        }
//
//        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
//            throw new AuthenticationException("Username or password error");
//        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
