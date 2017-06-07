package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.MenuOperation;
import cn.xtits.xtp.entity.MenuOperationExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.MenuOperationService;
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
@RequestMapping("/menuOperation")
public class MenuOperationController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private MenuOperationService service;

    @RequestMapping(value = "insertMenuOperation", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertMenuOperation(
            @RequestParam(value = "data", required = false) String data) {
        MenuOperation record = JsonUtil.fromJson(data, MenuOperation.class);
        record.setDeleteFlag(false);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteMenuOperation", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteMenuOperation(
            @RequestParam(value = "id", required = false) int id) {
        MenuOperation record = new MenuOperation();
        record.setDeleteFlag(true);
        record.setId(id);
        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateMenuOperation", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateMenuOperation(
            @RequestParam(value = "data", required = false) String data) {
        MenuOperation record = JsonUtil.fromJson(data, MenuOperation.class);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listMenuOperation")
    @ResponseBody
    public AjaxResult listMenuOperation(
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        MenuOperationExample example = new MenuOperationExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        MenuOperationExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        if (menuId != null && menuId > 0) {
            criteria.andMenuIdEqualTo(menuId);
        }
        List<MenuOperation> list = service.listByExample(example);
        Pagination<MenuOperation> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}