package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.Log;
import cn.xtits.xtp.entity.LogExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.LogService;
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
@RequestMapping("/log")
public class LogController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private LogService service;

    @RequestMapping(value = "insertLog")
    @ResponseBody
    public AjaxResult insertLog(
            @RequestParam(value = "data", required = false) String data) {
        Log record = JsonUtil.fromJson(data, Log.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteLog")
    @ResponseBody
    public AjaxResult deleteLog(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateLog")
    @ResponseBody
    public AjaxResult updateLog(
            @RequestParam(value = "data", required = false) String data) {
        Log record = JsonUtil.fromJson(data, Log.class);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listLog")
    @ResponseBody
    public AjaxResult listLog(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        LogExample example = new LogExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        LogExample.Criteria criteria = example.createCriteria();
        List<Log> list = service.listByExample(example);
        Pagination<Log> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}