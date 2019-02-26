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
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @RequiresPermissions({"user:insert"})
    @RequestMapping(value = "insertUser")
    @ResponseBody
    public AjaxResult insertUser(
            @RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "appCode", required = false) String appCode) {
        String password = Randoms.getRandomString(8);
        User record = JsonUtil.fromJson(data, User.class);

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andAccountEqualTo(record.getAccount());
        if (StringUtils.isBlank(appCode)) {
            if (record.getAppId() != null && record.getAppId() > 0) {
                criteria.andAppIdEqualTo(record.getAppId());
            }
        } else {
            App app = appService.getAppByCode(appCode);
            criteria.andAppIdEqualTo(app.getId());
            record.setAppId(app.getId());
        }
        List<User> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(ErrorCodeEnums.RECORD_EXISTS.value, ErrorCodeEnums.RECORD_EXISTS.msg);
        }
        record.setDeleteFlag(false);
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        record.setPassword(password);
        service.insert(record);
        return new AjaxResult(record);
    }

    //    @RequiresPermissions({"user:reset-password"})
    @RequestMapping(value = "updateUserResetPassword")
    @ResponseBody
    public AjaxResult updateUserResetPassword(
            @RequestParam(value = "id", required = false) Integer id) {
        String password = Randoms.getRandomString(8);
        User user = new User();
        user.setPassword(password);
        user.setId(id);
        int row = service.updateByPrimaryKeySelective(user);
        return new AjaxResult(row > 0 ? 1 : 0, "", user);
    }

    //@RequiresPermissions({"user:update-password"})
    @RequestMapping(value = "updateUserPassword")
    @ResponseBody
    public AjaxResult updateUserPassword(
            @RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "password", required = false) String password) {

        if (StringUtils.isBlank(password)) {
            return new AjaxResult(0, "旧密码不能为空!");
        }
        User record = JsonUtil.fromJson(data, User.class);
        if (StringUtils.isBlank(record.getPassword())) {
            return new AjaxResult(0, "密码不能为空!");
        }
        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andAccountEqualTo(record.getAccount());
        criteria.andPasswordEqualTo(password);

        App app = appService.getAppByToken(getAppToken());
        record.setAppId(app.getId());
        criteria.andAppIdEqualTo(app.getId());

        List<User> list = service.listByExample(example);
        if (list.size() > 0) {
            record.setId(list.get(0).getId());
            record.setModifyDate(new Date());
            service.updateByPrimaryKeySelective(record);
            return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
        } else {
            return new AjaxResult(0, "账号或密码不正确!");
        }
    }

    @RequestMapping(value = "updateSyncUser")
    @ResponseBody
    public AjaxResult updateSyncUser(
            @RequestParam(value = "appToken", required = false) String appToken,
            @RequestParam(value = "appCode", required = false) String appCode,
            @RequestParam(value = "data", required = false) String data) {

        User record = JsonUtil.fromJson(data, User.class);
        record.setModifyDate(new Date());
        App app = null;
        if (StringUtils.isNotBlank(appCode)) {
            app = appService.getAppByCode(appCode);
        } else if (StringUtils.isNotBlank(appToken)) {
            app = appService.getAppByToken(appToken);
        } else {
            appService.getAppByToken(getAppToken());
        }
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

    @RequiresPermissions({"user:delete"})
    @RequestMapping(value = "deleteUser")
    @ResponseBody
    public AjaxResult deleteUser(
            @RequestParam(value = "id", required = false) int id) {
        User record = service.getByPrimaryKey(id);
        record.setModifyDate(new Date());
        record.setDeleteFlag(true);
        service.updateByPrimaryKey(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequiresPermissions({"user:update"})
    @RequestMapping(value = "updateUser")
    @ResponseBody
    public AjaxResult updateUser(
            @RequestParam(value = "data", required = false) String data) {
        User record = JsonUtil.fromJson(data, User.class);
        record.setModifyDate(new Date());
        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    //@RequiresPermissions({"user:list"})
    @RequestMapping(value = "listUser")
    @ResponseBody
    public AjaxResult listUser(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "appFlag", required = false) Boolean appFlag,
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "appCode", required = false) String appCode,
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

        if (StringUtils.isNotBlank(id)) {
            String[] split = id.split(",");
            if (split.length > 1) {
                List<Integer> integerList = new ArrayList<>();
                for (String s : split) {
                    integerList.add(Integer.parseInt(s));
                }
                criteria.andIdIn(integerList);
            } else {
                criteria.andIdEqualTo(Integer.parseInt(split[0]));
            }
        }
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
        //appFlag 为true，不需要AppId查询
        if (appFlag == null || !appFlag) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        if ((appFlag == null || !appFlag) && !APP_TOKEN.equals(getAppToken())) {
            App app = appService.getAppByToken(getAppToken());
            criteria.andAppIdEqualTo(app.getId());
        }
        if (StringUtils.isNotBlank(appCode)) {
            App app = appService.getAppByCode(appCode.trim());
            criteria.andAppIdEqualTo(app.getId());
        }
        List<User> list = service.listByExample(example);
        Pagination<User> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

    //@RequiresPermissions({"user:list"})
    @RequestMapping(value = "listUserByRoleId")
    @ResponseBody
    public AjaxResult listUserByRoleId(
            @RequestParam(value = "roleId", required = false) String roleId) {
        List<Integer> roleIds = new ArrayList<>();
        String[] split = roleId.split(",");
        for (String s : split) {
            roleIds.add(Integer.parseInt(s));
        }
        List<User> list = service.listUserByRoleId(roleIds);
        Pagination<User> pList = new Pagination<>(list);
        return new AjaxResult(pList);
    }

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping(value = "loginUser")
    @ResponseBody
    public AjaxResult loginUser(
            @RequestParam(value = "appCode", required = false) String appCode,
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password", required = false) String password) {
        App app = null;
        if (StringUtils.isBlank(appCode)) {
            //app = appService.getAppByToken(APP_TOKEN);
        } else {
            app = appService.getAppByCode(appCode);
        }
//        if (app == null || app.getId() < 1) {
//            return new AjaxResult(-1, "该应用不存在!");
//        }
        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(account);
        criteria.andPasswordEqualTo(password);
        //criteria.andAppIdEqualTo(app.getId());
        List<User> list = service.listByExample(example);

        if (list.size() > 0) {
            User u = list.get(0);
            try {
                if (app == null) {
                    app = appService.getByPrimaryKey(u.getAppId());
                }
                LoginToken t = new LoginToken();
                t.setAppId(u.getAppId());
                t.setUserId(u.getId());
                t.setUserName(u.getName());
                t.setAppToken(app.getToken());
                String authToken = JsonUtil.toJson(t);
                String token = JwtUtil.createJWT(u.getId().toString(), authToken, 24 * 60 * 60 * 1000);
                try {
                    String key = "jwt:" + String.valueOf(u.getId());
                    ValueOperations<String, String> ops = template.opsForValue();
                    ops.set(key, token, 60 * 24, TimeUnit.MINUTES);
                } catch (Exception e) {

                }
                return new AjaxResult(ErrorCodeEnums.NO_ERROR.value, "", token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AjaxResult(ErrorCodeEnums.NORMAL_ERROR.value, "登录失败");

    }

    //@RequiresPermissions({"user:list"})
    @RequestMapping(value = "getUser")
    @ResponseBody
    public AjaxResult getUser(
            @RequestParam(value = "id", required = false) Integer id) {

        if (id == null) {
            id = getUserId();
        }
        User user = service.getByPrimaryKey(id);
        return new AjaxResult(user);
    }

    //@RequiresPermissions({"user:list"})
    @RequestMapping(value = "getUserByAppUserId")
    @ResponseBody
    public AjaxResult getUserByAppUserId(
            @RequestParam(value = "appToken", required = false) String appToken,
            @RequestParam(value = "appCode", required = false) String appCode,
            @RequestParam(value = "userId", required = false) Integer userId) {

        UserExample example = new UserExample();
        example.setPageSize(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andAppUserIdEqualTo(userId == null ? getUserId() : userId);
        App app;
        if (StringUtils.isNotBlank(appToken)) {
            app = appService.getAppByToken(appToken);
        } else if (StringUtils.isNotBlank(appCode)) {
            app = appService.getAppByCode(appCode);
        } else {
            app = appService.getAppByToken(getAppToken());
        }
        criteria.andAppIdEqualTo(app.getId());
        List<User> list = service.listByExample(example);
        if (list.size() > 0) {
            return new AjaxResult(list.get(0));
        } else {
            return new AjaxResult(-1, "不存在该用户!");
        }
    }

}