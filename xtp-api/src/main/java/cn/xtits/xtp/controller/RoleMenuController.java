package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.MapperUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.dto.RoleMenuDto;
import cn.xtits.xtp.entity.Menu;
import cn.xtits.xtp.entity.RoleMenu;
import cn.xtits.xtp.entity.RoleMenuExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.MenuService;
import cn.xtits.xtp.service.RoleMenuService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/roleMenu")
public class RoleMenuController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private RoleMenuService service;

    @Autowired
    private MenuService menuService;

    @Deprecated
    @RequestMapping(value = "insertRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertRoleMenu(
            @RequestParam(value = "data", required = false) String data) {
        RoleMenu record = JsonUtil.fromJson(data, RoleMenu.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @Deprecated
    @RequestMapping(value = "deleteRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteRoleMenu(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateRoleMenu(
            @RequestParam(value = "roleId") Integer roleId,
            @RequestParam(value = "menuIds") String menuIds) {
        service.updateRoleMenu(roleId, menuIds);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "listRoleMenu")
    @ResponseBody
    public AjaxResult listRoleMenu(
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        RoleMenuExample example = new RoleMenuExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        RoleMenuExample.Criteria criteria = example.createCriteria();

        if (roleId != null && roleId > 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        List<RoleMenu> list = service.listByExample(example);
        List<RoleMenuDto> dtoList = new ArrayList<>();
        for (RoleMenu roleMenu : list) {
            RoleMenuDto dto = new RoleMenuDto();
            try {
                MapperUtil.copyProperties(roleMenu, dto);

                Menu menu = menuService.getByPrimaryKey(roleMenu.getMenuId());
                dto.setMenuCode(menu.getCode());
                dto.setParentId(menu.getParentId());
                dto.setMenuName(menu.getName());
            } catch (Throwable e) {
            }
            dtoList.add(dto);
        }

        Pagination<RoleMenuDto> pList = new Pagination<>(example, dtoList, example.getCount());
        return new AjaxResult(pList);
    }

}