package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.UserFavorite;
import cn.xtits.xtp.entity.UserFavoriteExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.UserFavoriteService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/userFavorite")
public class UserFavoriteController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private UserFavoriteService service;

    @RequestMapping(value = "insertUserFavorite", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertUserFavorite(
            @RequestParam(value = "data", required = false) String data) {
        UserFavorite record = JsonUtil.fromJson(data, UserFavorite.class);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteUserFavorite", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteUserFavorite(
            @RequestParam(value = "id", required = false) int id) {
        service.deleteByPrimaryKey(id);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateUserFavorite", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUserFavorite(
            @RequestParam(value = "userId") Integer userId,
            @RequestParam(value = "data", required = false) String data) {

        List<UserFavorite> list = gson.fromJson(data, new TypeToken<List<UserFavorite>>() {
        }.getType());

        service.updateUserFavorite(userId, list);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "listUserFavorite")
    @ResponseBody
    public AjaxResult listUserFavorite(
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        UserFavoriteExample example = new UserFavoriteExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        UserFavoriteExample.Criteria criteria = example.createCriteria();
        List<UserFavorite> list = service.listByExample(example);
        Pagination<UserFavorite> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

}