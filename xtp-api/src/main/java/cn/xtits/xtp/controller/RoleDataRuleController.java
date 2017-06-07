package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.RoleDataRule;
import cn.xtits.xtp.entity.RoleDataRuleExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.RoleDataRuleService;
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
@RequestMapping("/roleDataRule")
public class RoleDataRuleController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private RoleDataRuleService service;

    @RequestMapping(value = "insertRoleDataRule", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertRoleDataRule(
            @RequestParam(value = "data", required = false) String data) {
        RoleDataRule record = JsonUtil.fromJson(data, RoleDataRule.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteRoleDataRule", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteRoleDataRule(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateRoleDataRule", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateRoleDataRule(
            @RequestParam(value = "data", required = false) String data) {
        RoleDataRule record = JsonUtil.fromJson(data, RoleDataRule.class);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listRoleDataRule")
    @ResponseBody
    public AjaxResult listRoleDataRule(
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        RoleDataRuleExample example = new RoleDataRuleExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        RoleDataRuleExample.Criteria criteria = example.createCriteria();
        if (roleId != null && roleId > 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        if (menuId != null && menuId > 0) {
            criteria.andMenuIdEqualTo(menuId);
        }
        List<RoleDataRule> list = service.listByExample(example);
        Pagination<RoleDataRule> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}