package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.Role;
import cn.xtits.xtp.entity.RoleExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.RoleService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    @Autowired
    private AppService appService;

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private RoleService service;

    @RequestMapping(value = "insertRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertRole(
            @RequestParam(value = "data", required = false) String data) {
        Role record = JsonUtil.fromJson(data, Role.class);
        if (APP_TOKEN != getAppToken()) {
            App app = appService.getAppByToken(getAppToken());
            record.setAppId(app.getId());
        }

        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andCodeEqualTo(record.getCode());
        if (record.getAppId() != null && record.getAppId() > 0) {
            criteria.andAppIdEqualTo(record.getAppId());
        }
        criteria.andIdNotEqualTo(record.getId());
        List<Role> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value);
        }
        example.clear();
        criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andNameEqualTo(record.getName());
        criteria.andAppIdEqualTo(record.getAppId());
        criteria.andIdNotEqualTo(record.getId());
        list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value);
        }

        record.setDeleteFlag(false);
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteRole(
            @RequestParam(value = "id", required = false) int id) {
        Role record = service.getByPrimaryKey(id);
        record.setModifyDate(new Date());
        record.setDeleteFlag(true);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateRole(
            @RequestParam(value = "data", required = false) String data) {
        Role record = JsonUtil.fromJson(data, Role.class);
        record.setModifyDate(new Date());
        if (APP_TOKEN != getAppToken()) {
            App app = appService.getAppByToken(getAppToken());
            record.setAppId(app.getId());
        }
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andCodeEqualTo(record.getCode());
        if (record.getAppId() != null && record.getAppId() > 0) {
            criteria.andAppIdEqualTo(record.getAppId());
        }
        criteria.andIdNotEqualTo(record.getId());
        List<Role> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value);
        }
        example.clear();
        criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andNameEqualTo(record.getName());
        criteria.andAppIdEqualTo(record.getAppId());
        criteria.andIdNotEqualTo(record.getId());
        list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value);
        }

        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listRole")
    @ResponseBody
    public AjaxResult listRole(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code) {
        RoleExample example = new RoleExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        RoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);

        if (!APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        if (name != null && !"".equals(name.trim())) {
            criteria.andNameLike(name);
        }
        if (code != null && !"".equals(code.trim())) {
            criteria.andCodeLike(code);
        }
        if (appId != null && appId > 0) {
            criteria.andAppIdEqualTo(appId);
        }
        List<Role> list = service.listByExample(example);
        Pagination<Role> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }
}