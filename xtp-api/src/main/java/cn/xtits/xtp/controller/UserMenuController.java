package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.UserMenu;
import cn.xtits.xtp.entity.UserMenuExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.UserMenuService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/userMenu")
public class UserMenuController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private UserMenuService service;

    @RequestMapping(value = "insertUserMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertUserMenu(
            @RequestParam(value = "data", required = false) String data) {
        UserMenu record = JsonUtil.fromJson(data, UserMenu.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteUserMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteUserMenu(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateUserMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUserMenu(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "data", required = false) String data) {
        List<UserMenu> list = gson.fromJson(data, new TypeToken<List<UserMenu>>() {
        }.getType());
        service.updateUserMenu(userId, list);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listUserMenu")
    @ResponseBody
    public AjaxResult listUserMenu(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        UserMenuExample example = new UserMenuExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        UserMenuExample.Criteria criteria = example.createCriteria();
        if (userId != null && userId > 0) {
            criteria.andUserIdEqualTo(userId);
        }
        List<UserMenu> list = service.listByExample(example);
        Pagination<UserMenu> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}