package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.MapperUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.dto.MenuDto;
import cn.xtits.xtp.dto.UserMenuDto;
import cn.xtits.xtp.dto.UserMenuWithOperationDto;
import cn.xtits.xtp.entity.*;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.MenuService;
import cn.xtits.xtp.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    @Autowired
    private UserService userService;

    @Autowired
    private AppService appService;

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "insertMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertMenu(
            @RequestParam(value = "data", required = false) String data) {
        Menu record = JsonUtil.fromJson(data, Menu.class);
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(record.getCode());
        criteria.andIdNotEqualTo(record.getId());
        criteria.andDeleteFlagEqualTo(false);
        List<Menu> list = menuService.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(0, "记录已经存在");
        }

        if (APP_TOKEN != getAppToken()) {
            App app = appService.getAppByToken(getAppToken());
            record.setAppId(app.getId());
        }

        record.setDeleteFlag(false);
        if (record.getParentId() != null && record.getParentId() > 0) {
            record.setParentFlag(false);
        } else {
            record.setParentFlag(true);
        }
        menuService.insert(record);
        return new AjaxResult(record);
    }

    @RequestMapping(value = "deleteMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteMenu(
            @RequestParam(value = "id", required = false) int id) {

        MenuExample example = new MenuExample();
        example.setPageIndex(1);
        example.setPageSize(1);
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        criteria.andDeleteFlagEqualTo(false);

        List<Menu> list = menuService.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.NORMAL_ERROR.value, "请先删除子菜单");
        }
        Menu record = menuService.getByPrimaryKey(id);
        record.setDeleteFlag(true);
        menuService.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateMenu(
            @RequestParam(value = "data", required = false) String data) {
        Menu record = JsonUtil.fromJson(data, Menu.class);
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(record.getCode());
        criteria.andIdNotEqualTo(record.getId());
        criteria.andDeleteFlagEqualTo(false);
        List<Menu> list = menuService.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(0, "记录已经存在");
        }

        record.setDeleteFlag(false);
        if (record.getParentId() != null && record.getParentId() > 0) {
            record.setParentFlag(true);
        } else {
            record.setParentFlag(true);
        }
        menuService.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listMenu")
    @ResponseBody
    public AjaxResult listMenu(
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Integer parentId) {
        MenuExample example = new MenuExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        MenuExample.Criteria criteria = example.createCriteria();
        if (name != null) {
            criteria.andNameLike(name);
        }
        if (code != null) {
            criteria.andCodeLike(code);
        }
        if (appId != null && appId > 0) {

            criteria.andAppIdEqualTo(appId);
        }
        if (!APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        criteria.andDeleteFlagEqualTo(false);
        criteria.andParentIdEqualTo(parentId);
        List<Menu> list = menuService.listByExample(example);
        List<MenuDto> rList = new ArrayList<>();
        for (Menu item : list) {
            MenuDto dto = new MenuDto();
            MapperUtil.copyProperties(item, dto);
            App app = appService.getByPrimaryKey(item.getAppId());
            if (app != null) {
                dto.setAppName(app.getName());
            }
            rList.add(dto);
        }
        Pagination<MenuDto> pList = new Pagination<>(example, rList, example.getCount());
        return new AjaxResult(pList);
    }

    @RequestMapping(value = "listMenuByUserId")
    @ResponseBody
    public AjaxResult listMenuByUserId(
            @RequestParam(value = "userId", required = false) Integer userId
    ) {
        App app = appService.getAppByToken(getAppToken());
        Integer appId = app.getId();
        if (userId != null && userId > 0) {
            User user = userService.getByPrimaryKey(userId);
            appId = user.getAppId();

        } else {
            userId = getUserId();
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAppUserIdEqualTo(userId);
        criteria.andAppIdEqualTo(appId);
        List<User> list = userService.listByExample(example);
        UserMenuDto dto = new UserMenuDto();
        if (list.size() > 0) {
            User u = list.get(0);
            try {
                dto = menuService.listMenuByUserId(u.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AjaxResult(dto);
    }

    @RequestMapping(value = "listMenuWithOperationByUserId")
    @ResponseBody
    public AjaxResult listMenuWithOperationByUserId(
            @RequestParam(value = "userId", required = false) Integer userId
    ) {
        App app = appService.getAppByToken(getAppToken());
        Integer appId = app.getId();
        Integer appUserId;
        if (userId != null && userId > 0) {
            User user = userService.getByPrimaryKey(userId);
            appId = user.getAppId();
            appUserId = user.getAppUserId();

        } else {
            userId = getUserId();
            appUserId = userId;
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAppUserIdEqualTo(appUserId);
        criteria.andAppIdEqualTo(appId);
        List<User> list = userService.listByExample(example);
        UserMenuWithOperationDto dto = new UserMenuWithOperationDto();
        if (list.size() > 0) {
            User u = list.get(0);
            try {
                dto = menuService.listMenuWithOperationByUserId(u.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AjaxResult(dto);
    }
}