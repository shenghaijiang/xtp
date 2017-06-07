package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.RoleUser;
import cn.xtits.xtp.entity.RoleUserExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.RoleUserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/roleUser")
public class RoleUserController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private RoleUserService service;

    @RequestMapping(value = "updateRoleUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateRoleUser(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "roleIds") String roleIds) {
        service.updateRoleUser(userId, roleIds);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "listRoleUser")
    @ResponseBody
    public AjaxResult listRoleUser(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        RoleUserExample example = new RoleUserExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        RoleUserExample.Criteria criteria = example.createCriteria();

        if (roleId != null && roleId > 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        if (userId != null && userId > 0) {
            criteria.andUserIdEqualTo(userId);
        }
        List<RoleUser> list = service.listByExample(example);
        Pagination<RoleUser> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }
}