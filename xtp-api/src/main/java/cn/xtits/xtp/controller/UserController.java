package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.JwtUtil;
import cn.xtits.xtf.common.utils.Randoms;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.entity.UserExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.interceptor.LoginToken;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.UserService;
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
@RequestMapping("/user")
public class UserController extends BaseController {

    @Value("${REST_XTP_URL}")
    private String REST_XTP_URL;

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private UserService service;

    @Autowired
    private AppService appService;

    @RequestMapping(value = "insertUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertUser(
            @RequestParam(value = "data", required = false) String data) {
        String password = Randoms.getRandomString(8);
        User record = JsonUtil.fromJson(data, User.class);

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andAccountEqualTo(record.getAccount());
        if (record.getId() != null && record.getId() > 0) {
            criteria.andIdNotEqualTo(record.getId());
        }
        List<User> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value, ErrorCodeEnums.RECORD_EXISTS.msg);
        }

        record.setDeleteFlag(false);
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        service.insert(record);
        record.setPassword(password);
        return new AjaxResult(record);
    }

    @RequestMapping(value = "updateUserResetPassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUserPassword(
            @RequestParam(value = "id", required = false) Integer userId) {
        String password = Randoms.getRandomString(8);
        User user = new User();
        user.setPassword(password);
        user.setId(userId);
        service.updateByPrimaryKeySelective(user);
        return new AjaxResult(user);
    }

    @RequestMapping(value = "updateUserPassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUserPassword(
            @RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "password", required = false) String password) {
        App app = appService.getAppByToken(APP_TOKEN);
        User record = JsonUtil.fromJson(data, User.class);
        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(record.getAccount());
        criteria.andPasswordEqualTo(password);
        criteria.andAppIdEqualTo(app.getId());
        List<User> list = service.listByExample(example);
        if (list.size() > 0) {
            record.setId(list.get(0).getId());
            record.setModifyDate(new Date());
            service.updateByPrimaryKeySelective(record);
            return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
        } else {
            return new AjaxResult(0);
        }
    }

    @RequestMapping(value = "updateSyncUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateSyncUser(
            @RequestParam(value = "appToken", required = false) String appToken,
            @RequestParam(value = "data", required = false) String data) {

        User record = JsonUtil.fromJson(data, User.class);
        record.setModifyDate(new Date());
        App app = appService.getAppByToken(appToken);
        User user = service.getUserByAppUserId(record.getId(), app.getId());
        if (null != user) {
            record.setId(user.getId());
            record.setAppId(user.getAppId());
            service.updateByPrimaryKeySelective(record);
        } else {
            if (record.getOrganizeId() == null) {
                record.setOrganizeId(0);
            }
            record.setAppId(app.getId());
            record.setAppUserId(record.getId());
            record.setId(null);
            record.setDeleteFlag(false);
            record.setCreateDate(new Date());
            service.insert(record);
        }

        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value, "", record.getId().toString());
    }

    @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteUser(
            @RequestParam(value = "id", required = false) int id) {
        User record = service.getByPrimaryKey(id);
        record.setModifyDate(new Date());
        record.setDeleteFlag(true);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUser(
            @RequestParam(value = "data", required = false) String data) {
        User record = JsonUtil.fromJson(data, User.class);
        record.setModifyDate(new Date());
        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }


    @RequestMapping(value = "listUser")
    @ResponseBody
    public AjaxResult listUser(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        UserExample example = new UserExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        if (account != null && !"".equals(account.trim())) {
            criteria.andAccountLike(account);
        }
        if (name != null && !"".equals(name.trim())) {
            criteria.andNameLike(name);
        }
        if (mail != null && !"".equals(mail.trim())) {
            criteria.andMailLike(mail);
        }
        if (phone != null && !"".equals(phone.trim())) {
            criteria.andPhoneLike(phone);
        }
        if (appId != null && appId > 0) {
            criteria.andAppIdEqualTo(appId);
        }
        if(!APP_TOKEN.equals(getAppToken())){
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        List<User> list = service.listByExample(example);
        Pagination<User> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

    @RequestMapping(value = "listUserByRoleId")
    @ResponseBody
    public AjaxResult listUser(
            @RequestParam(value = "roleId", required = false) Integer roleId) {
        List<User> list = service.listUserByRoleId(roleId);
        Pagination<User> pList = new Pagination<>(list);
        return new AjaxResult(pList);
    }

    @RequestMapping(value = "loginUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUser(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password", required = false) String password) {
        App app = appService.getAppByToken(APP_TOKEN);
        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(account);
        criteria.andPasswordEqualTo(password);
        criteria.andAppIdEqualTo(app.getId());
        List<User> list = service.listByExample(example);

        if (list.size() > 0) {
            User u = list.get(0);
            try {
                LoginToken t = new LoginToken();
                t.setUserId(u.getId());
                t.setUserName(u.getAccount());
                t.setAppToken(APP_TOKEN);
                String token = JwtUtil.createJWT(u.getId().toString(), JsonUtil.toJson(t), 24 * 60 * 60 * 1000);
                return new AjaxResult(ErrorCodeEnums.NO_ERROR.value, "", token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AjaxResult(ErrorCodeEnums.NORMAL_ERROR.value, "登录失败");

    }

    @RequestMapping(value = "getUser", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getUser(
            @RequestParam(value = "id", required = false) Integer id) {
        User user = service.getByPrimaryKey(id);
        return new AjaxResult(user);
    }

    @RequestMapping(value = "getUserByAppUserId")
    @ResponseBody
    public AjaxResult getUserByAppUserId(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "appToken", required = false) String appToken) {
        App app = appService.getAppByToken(appToken);
        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
            criteria.andAppUserIdEqualTo(id);

            criteria.andAppIdEqualTo(app.getId());
        List<User> list = service.listByExample(example);
        if(list.size()>0) {
            return new AjaxResult(list.get(0));
        }else {
            return new AjaxResult(null);
        }
    }

}