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
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions({"menu:insert"})
    @RequestMapping(value = "insertMenu")
    @ResponseBody
    public AjaxResult insertMenu(
            @RequestParam(value = "data", required = false) String data) {
        Menu record = JsonUtil.fromJson(data, Menu.class);
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        if (record.getId() != null && record.getId() > 0) {
            criteria.andAppIdEqualTo(record.getAppId());
        }
        criteria.andCodeEqualTo(record.getCode());
        //criteria.andIdNotEqualTo(record.getId());
        criteria.andDeleteFlagEqualTo(false);
        List<Menu> list = menuService.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(0, "记录已经存在");
        }

        if (!APP_TOKEN.equals(getAppToken())) {
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

    @RequiresPermissions({"menu:delete"})
    @RequestMapping(value = "deleteMenu")
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

//    @RequiresPermissions({"menu:copy"})
    @RequestMapping(value = "updateCopy")
    @ResponseBody
    public AjaxResult deleteMenu(
            @RequestParam(value = "fromAppId", required = false) int fromAppId,
            @RequestParam(value = "toAppId", required = false) int toAppId) {

        MenuExample example = new MenuExample();
        example.setPageIndex(1);
        example.setPageSize(1);
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(toAppId);
        criteria.andDeleteFlagEqualTo(false);

        List<Menu> list = menuService.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.NORMAL_ERROR.value, "请先删除目标系统的菜单");
        }
        int i = menuService.updateCopy(fromAppId, toAppId);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"menu:update"})
    @RequestMapping(value = "updateMenu")
    @ResponseBody
    public AjaxResult updateMenu(
            @RequestParam(value = "data", required = false) String data) {
        Menu record = JsonUtil.fromJson(data, Menu.class);
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        if (record.getId() != null && record.getId() > 0) {
            criteria.andAppIdEqualTo(record.getAppId());
        }
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

    //@RequiresPermissions({"menu:list"})
    @RequestMapping(value = "checkExists")
    @ResponseBody
    public AjaxResult checkExists(
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "code", required = false) String code) {
        MenuExample example = new MenuExample();
        example.setPageIndex(1);
        example.setPageSize(1);
        MenuExample.Criteria criteria = example.createCriteria();
        if (name!=null && !"".equals(name)) {
            criteria.andNameEqualTo(name);
        }
        if (code!=null && !"".equals(code)) {
            criteria.andCodeEqualTo(code);
        }
        if (appId != null && appId > 0) {
            criteria.andAppIdEqualTo(appId);
        }
        if (id != null && id > 0) {
            criteria.andIdNotEqualTo(id);
        }
        if (!APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        criteria.andDeleteFlagEqualTo(false);
        List<Menu> list = menuService.listByExample(example);
        boolean flag = false;
        if(list.size()>0)
        {
            flag = true;
        }
        return new AjaxResult(flag);
    }

    //@RequiresPermissions({"menu:list"})
    @RequestMapping(value = "listMenu")
    @ResponseBody
    public AjaxResult listMenu(
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Integer parentId) {
        MenuExample example = new MenuExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        example.setOrderByClause("parentId,sort asc");
        MenuExample.Criteria criteria = example.createCriteria();
        if (name!=null && !"".equals(name)) {
            criteria.andNameLike(name);
        }
        if (code!=null && !"".equals(code)) {
            criteria.andCodeLike(code);
        }
        if (appId != null && appId > 0) {

            criteria.andAppIdEqualTo(appId);
        }
        if(parentId!=null)
        {
            criteria.andParentIdEqualTo(parentId);
        }
        if (!APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        criteria.andDeleteFlagEqualTo(false);
        List<Menu> list = menuService.listByExample(example);

        Pagination<Menu> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }


    @RequestMapping(value = "listMenuWithChildren")
    @ResponseBody
    public AjaxResult listMenuWithChildren(
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "parentId", required = false, defaultValue = "0") Integer parentId) {
        MenuExample example = new MenuExample();
        example.setPageIndex(1);
        example.setPageSize(Integer.MAX_VALUE);
        example.setOrderByClause("parentId,sort asc");
        MenuExample.Criteria criteria = example.createCriteria();
        if (name != null && !"".equals(name)) {
            criteria.andNameLike(name);
        }
        if (code != null && !"".equals(code)) {
            criteria.andCodeLike(code);
        }
        if (appId != null && appId > 0) {

            criteria.andAppIdEqualTo(appId);
        }
        if (parentId > 0) {
            criteria.andParentIdGreaterThan(0);
        }
        if (!APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        criteria.andDeleteFlagEqualTo(false);
        //criteria.andParentIdEqualTo(parentId);
        List<Menu> list = menuService.listByExample(example);

        List<MenuDto> rList = new ArrayList<>();

        // 一级
        for (Menu itemL1 : list) {
            if (itemL1.getParentId().equals(parentId)) {
                MenuDto dto = new MenuDto();
                MapperUtil.copyProperties(itemL1, dto);
                rList.add(dto);
            }
        }

        // 二级
        for (MenuDto itemL1 : rList) {
            itemL1.setChildren(new ArrayList<MenuDto>());
            for (Menu itemL2 : list) {
                if (itemL2.getParentId().equals(itemL1.getId())) {
                    MenuDto dto = new MenuDto();
                    MapperUtil.copyProperties(itemL2, dto);
                    itemL1.getChildren().add(dto);
                }
            }
        }

        // 三级
        for (MenuDto itemL1 : rList) {
            for (MenuDto itemL2 : itemL1.getChildren()) {
                itemL2.setChildren(new ArrayList<MenuDto>());
                for (Menu itemL3 : list) {
                    if (itemL3.getParentId().equals(itemL2.getId())) {
                        MenuDto dto = new MenuDto();
                        MapperUtil.copyProperties(itemL3, dto);
                        itemL2.getChildren().add(dto);
                    }
                }
            }
        }


        // 四级
        for (MenuDto itemL1 : rList) {
            for (MenuDto itemL2 : itemL1.getChildren()) {
                for (MenuDto itemL3 : itemL2.getChildren()) {
                    itemL3.setChildren(new ArrayList<MenuDto>());
                    for (Menu itemL4 : list) {
                        if (itemL4.getParentId().equals(itemL3.getId())) {
                            MenuDto dto = new MenuDto();
                            MapperUtil.copyProperties(itemL4, dto);
                            itemL3.getChildren().add(dto);
                        }
                    }
                }
            }
        }

        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);

        example.setCount(rList.size());
        if (rList.size() > (pageIndex - 1) * pageSize) {


            if (rList.size() - (pageIndex - 1) * pageSize < pageSize) {
                rList = rList.subList((pageIndex - 1) * pageSize, rList.size());
            } else {
                rList = rList.subList((pageIndex - 1) * pageSize, pageIndex * pageSize);
            }

        }


        Pagination<MenuDto> pList = new Pagination<>(example, rList, example.getCount());
        return new AjaxResult(pList);
    }

    //@RequiresPermissions({"menu:list"})
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

    //@RequiresPermissions({"menu:list"})
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
        return new AjaxResult(dto,true);
    }
}