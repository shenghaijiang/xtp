package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.AppFavorite;
import cn.xtits.xtp.entity.AppFavoriteExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppFavoriteService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/appFavorite")
public class AppFavoriteController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private AppFavoriteService service;

    @RequiresPermissions({"app-favorite:insert"})
    @Deprecated
    @RequestMapping(value = "insertAppFavorite")
    @ResponseBody
    public AjaxResult insertAppFavorite(
            @RequestParam(value = "data", required = false) String data) {
        AppFavorite record = JsonUtil.fromJson(data, AppFavorite.class);
        AppFavoriteExample example = new AppFavoriteExample();
        AppFavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(record.getAppId());
        criteria.andMenuIdEqualTo(record.getMenuId());
        //criteria.andIdNotEqualTo(record.getId());
        List<AppFavorite> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value, ErrorCodeEnums.RECORD_EXISTS.msg);
        }

        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"app-favorite:delete"})
    @Deprecated
    @RequestMapping(value = "deleteAppFavorite")
    @ResponseBody
    public AjaxResult deleteAppFavorite(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"app-favorite:update"})
    @RequestMapping(value = "updateAppFavorite")
    @ResponseBody
    public AjaxResult updateAppFavorite(
            @RequestParam(value = "appId") Integer appId,
            @RequestParam(value = "menuIds") String menuIds) {
        service.updateAppFavorite(appId, menuIds);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"app-favorite:list"})
    @Deprecated
    @RequestMapping(value = "listAppFavorite")
    @ResponseBody
    public AjaxResult listAppFavorite(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        AppFavoriteExample example = new AppFavoriteExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        AppFavoriteExample.Criteria criteria = example.createCriteria();
        List<AppFavorite> list = service.listByExample(example);
        Pagination<AppFavorite> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}