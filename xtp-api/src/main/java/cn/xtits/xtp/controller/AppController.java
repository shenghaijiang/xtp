package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//import io.swagger.annotations.*;

//@Api(value ="/app",description = "")
@Controller
@RequestMapping("/app")
public class AppController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private AppService service;

    @RequiresPermissions({"app:insert"})
    @RequestMapping(value = "insertApp")
    @ResponseBody
    public AjaxResult insertApp(
            @RequestParam(value = "data", required = false) String data) {
        App record = JsonUtil.fromJson(data, App.class);
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(record.getCode());
        //criteria.andIdNotEqualTo(record.getId());
        List<App> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value, "记录已经存在");
        }
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"app:delete"})
    @RequestMapping(value = "deleteApp")
    @ResponseBody
    public AjaxResult deleteApp(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"app:update"})
    @RequestMapping(value = "updateApp")
    @ResponseBody
    public AjaxResult updateApp(
            @RequestParam(value = "data", required = false) String data) {
        App record = JsonUtil.fromJson(data, App.class);
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(record.getCode());
        criteria.andIdNotEqualTo(record.getId());
        List<App> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value, "记录已经存在");
        }
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"app:list"})
    @RequestMapping(value = "listApp")
    @ResponseBody
    public AjaxResult listApp(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        AppExample example = new AppExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        AppExample.Criteria criteria = example.createCriteria();
        if (name != null && !"".equals(name.trim())) {
            criteria.andNameLike(name);
        }
        if (code != null && !"".equals(code.trim())) {
            criteria.andCodeLike(code);
        }
        List<App> list = service.listByExample(example);
        Pagination<App> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }
}