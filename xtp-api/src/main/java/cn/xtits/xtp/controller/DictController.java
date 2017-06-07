package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.Dict;
import cn.xtits.xtp.entity.DictExample;
import cn.xtits.xtp.enums.DictTypeEnums;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.DictService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private DictService service;

    @RequestMapping(value = "insertDict", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertDict(
            @RequestParam(value = "data", required = false) String data) {
        Dict record = JsonUtil.fromJson(data, Dict.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteDict", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteDict(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateDict", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateDict(
            @RequestParam(value = "data", required = false) String data) {
        Dict record = JsonUtil.fromJson(data, Dict.class);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listDict")
    @ResponseBody
    public AjaxResult listDict(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        DictExample example = new DictExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        DictExample.Criteria criteria = example.createCriteria();
        List<Dict> list = service.listByExample(example);
        Pagination<Dict> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

    @RequestMapping(value = "listDictType")
    @ResponseBody
    public AjaxResult listDictType() {
        Map<String, String> map = new HashMap<>();
        for (DictTypeEnums e : DictTypeEnums.values()) {

            map.put(e.value, e.msg);
        }
        return new AjaxResult(map);
    }
}