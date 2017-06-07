package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.Organize;
import cn.xtits.xtp.entity.OrganizeExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.OrganizeService;
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
@RequestMapping("/organize")
public class OrganizeController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private OrganizeService service;

    @RequestMapping(value = "insertOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertOrganize(
            @RequestParam(value = "data", required = false) String data) {
        Organize record = JsonUtil.fromJson(data, Organize.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteOrganize(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateOrganize(
            @RequestParam(value = "data", required = false) String data) {
        Organize record = JsonUtil.fromJson(data, Organize.class);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listOrganize")
    @ResponseBody
    public AjaxResult listOrganize(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        OrganizeExample example = new OrganizeExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        OrganizeExample.Criteria criteria = example.createCriteria();
        List<Organize> list = service.listByExample(example);
        Pagination<Organize> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}