package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.UserMenuOperation;
import cn.xtits.xtp.entity.UserMenuOperationExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.UserMenuOperationService;
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
@RequestMapping("/userMenuOperation")
public class UserMenuOperationController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private UserMenuOperationService service;

    //@RequiresPermissions({"user-menu-operation:insert"})
    @RequestMapping(value = "insertUserMenuOperation")
    @ResponseBody
    public AjaxResult insertUserMenuOperation(
            @RequestParam(value = "data", required = false) String data) {
        UserMenuOperation record = JsonUtil.fromJson(data, UserMenuOperation.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"user-menu-operation:delete"})
    @RequestMapping(value = "deleteUserMenuOperation")
    @ResponseBody
    public AjaxResult deleteUserMenuOperation(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"user-menu-operation:update"})
    @RequestMapping(value = "updateUserMenuOperation")
    @ResponseBody
    public AjaxResult updateUserMenuOperation(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "menuId") Integer menuId,
            @RequestParam(value = "data", required = false) String data) {
        List<UserMenuOperation> list = gson.fromJson(data, new TypeToken<List<UserMenuOperation>>() {
        }.getType());
        service.updateUserMenuOperation(userId, menuId, list);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"user-menu-operation:list"})
    @RequestMapping(value = "listUserMenuOperation")
    @ResponseBody
    public AjaxResult listUserMenuOperation(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        UserMenuOperationExample example = new UserMenuOperationExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        UserMenuOperationExample.Criteria criteria = example.createCriteria();
        if (userId != null && userId > 0) {
            criteria.andUserIdEqualTo(userId);
        }
        if (menuId != null && menuId > 0) {
            criteria.andMenuIdEqualTo(menuId);
        }
        List<UserMenuOperation> list = service.listByExample(example);
        Pagination<UserMenuOperation> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}