package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.RoleMenuOperation;
import cn.xtits.xtp.entity.RoleMenuOperationExample;
import cn.xtits.xtp.entity.UserMenuOperation;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.RoleMenuOperationService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/roleMenuOperation")
public class RoleMenuOperationController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private RoleMenuOperationService service;

    //@RequiresPermissions({"role-menu-operation:insert"})
    @RequestMapping(value = "insertRoleMenuOperation")
    @ResponseBody
    public AjaxResult insertRoleMenuOperation(
            @RequestParam(value = "data", required = false) String data) {
        RoleMenuOperation record = JsonUtil.fromJson(data, RoleMenuOperation.class);
        //Integer	menuOperationId = record.getMenuOperationId();
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value, "", record);
    }

    //@RequiresPermissions({"role-menu-operation:delete"})
    @RequestMapping(value = "deleteRoleMenuOperation")
    @ResponseBody
    public AjaxResult deleteRoleMenuOperation(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"role-menu-operation:update"})
    @RequestMapping(value = "updateRoleMenuOperation")
    @ResponseBody
    public AjaxResult updateRoleMenuOperation(
            @RequestParam(value = "roleId") Integer roleId,
            @RequestParam(value = "data", required = false) String data) {
        List<RoleMenuOperation> list = gson.fromJson(data, new TypeToken<List<RoleMenuOperation>>() {
        }.getType());
        service.updateRoleMenuOperation(roleId, list);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"role-menu-operation:list"})
    @RequestMapping(value = "listRoleMenuOperation")
    @ResponseBody
    public AjaxResult listRoleMenuOperation(
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        RoleMenuOperationExample example = new RoleMenuOperationExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        RoleMenuOperationExample.Criteria criteria = example.createCriteria();
        if (roleId != null && roleId > 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        if (menuId != null && menuId > 0) {
            criteria.andMenuIdEqualTo(menuId);
        }
        List<RoleMenuOperation> list = service.listByExample(example);
        Pagination<RoleMenuOperation> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}