package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.dto.MenuWithOperationDto;
import cn.xtits.xtp.dto.ShiroDto;
import cn.xtits.xtp.dto.UserMenuWithOperationDto;
import cn.xtits.xtp.entity.MenuOperation;
import cn.xtits.xtp.entity.RoleUser;
import cn.xtits.xtp.entity.RoleUserExample;
import cn.xtits.xtp.service.MenuService;
import cn.xtits.xtp.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/shiro")
public class ShiroController {

    @Autowired
    private StringRedisTemplate template;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleUserService roleUserService;

    @RequestMapping(value = "refreshPermissions")
    @ResponseBody
    public AjaxResult refreshPermissions(
            @RequestParam(value = "userId", required = false) Integer userId) {
        if (userId != null) {
            String key = userId.toString();
            template.delete(key);
            Boolean hasKey = template.hasKey(key);
            if (hasKey) {
                return new AjaxResult(-1);
            }
        } else {
            Set<String> keys = template.keys("auth:*");
            for (String key : keys) {
                template.delete(key);
            }
        }
        return new AjaxResult(1);
    }

    @RequestMapping(value = "listMenuWithOperationByUserId")
    @ResponseBody
    public AjaxResult listMenuWithOperationByUserId(
            @RequestParam(value = "userId", required = false) Integer userId) {
//        template.delete(template.keys("*"));
        RoleUserExample roleUserExample = new RoleUserExample();
        RoleUserExample.Criteria criteria = roleUserExample.createCriteria();
        criteria.andUserIdEqualTo(userId);

        List<RoleUser> roleUserList = roleUserService.listByExample(roleUserExample);

        String role = "";
        for (RoleUser roleUser : roleUserList) {
            role += roleUser.getRoleId() + ",";
        }


        UserMenuWithOperationDto menuList = menuService.listMenuWithOperationByUserId(userId);

        String permission = "";

        for (MenuWithOperationDto item : menuList.getAllMenu()) {
            String menu = "";
            List<MenuOperation> operationList = item.getOperationList();
            menu += item.getCode() + ":";
            if (operationList.size() > 0) {
                for (MenuOperation menuOperation : operationList) {
                    menu += menuOperation.getCode() + ",";
                }
                menu = menu.substring(0, menu.length() - 1);
                permission += menu + "|";
            }
        }

        ShiroDto shiroDto = new ShiroDto();
        shiroDto.setPermissions(permission.substring(0, permission.length() - 1));
        shiroDto.setRoles(role.substring(0, role.length() - 1));
        return new AjaxResult(shiroDto);
    }
}
