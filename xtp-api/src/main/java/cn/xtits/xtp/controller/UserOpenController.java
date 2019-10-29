package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.HttpClientUtil;
import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.JwtUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.UserOpen;
import cn.xtits.xtp.entity.UserOpenExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.interceptor.LoginToken;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.UserOpenService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/userOpen")
public class UserOpenController extends BaseController {

    @Value("${REST_XTP_URL}")
    private String REST_XTP_URL;

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    @Value("${wx.appid}")
    private String APPID;

    @Value("${wx.secret}")
    private String SECRET;

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private UserOpenService service;

    @Autowired
    private AppService appService;

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping(value = "loginUserOpen")
    @ResponseBody
    public AjaxResult loginUserOpen(
            @RequestParam(value = "appId", required = false) Integer appId,
            @RequestParam(value = "openType", required = false) String openType,
            @RequestParam(value = "openId", required = false) String openId,
            @RequestParam(value = "accessToken") String accessToken,
            @RequestParam(value = "avatar", required = false) String avatar,
            @RequestParam(value = "nickName", required = false) String nickName) {

        UserOpenExample example = new UserOpenExample();
        example.setPageSize(1);
        UserOpenExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        criteria.andOpenIdEqualTo(openId);
        criteria.andOpenTypeEqualTo(openType);
        if(appId!=null){
            criteria.andAppIdEqualTo(appId);
        }
        //criteria.andAppIdEqualTo(app.getId());
        List<UserOpen> list = service.listByExample(example);
        App app  = appService.getByPrimaryKey(appId);

        if (list.size() > 0) {
            UserOpen u = list.get(0);
            try {
                LoginToken t = new LoginToken();
                t.setAppId(u.getAppId());
                t.setUserId(u.getUserId());
                t.setUserName(u.getNickName());
                t.setAppToken(app.getToken());
                String authToken = JsonUtil.toJson(t);
                String token = JwtUtil.createJWT(u.getUserId().toString(), authToken, 24 * 60 * 60 * 1000);
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
        }else {

            UserOpen u = new UserOpen();
            u.setAppId(appId);
            u.setAvatar(avatar);
            u.setOpenId(openId);
            u.setOpenType(openType);
            u.setNickName(nickName);
            u.setAccessToken(accessToken);
            u.setDeleteFlag(false);
            service.insertUserOpen(u);
            try {
                LoginToken t = new LoginToken();
                t.setAppId(u.getAppId());
                t.setUserId(u.getUserId());
                t.setUserName(u.getNickName());
                t.setAppToken(app.getToken());
                String authToken = JsonUtil.toJson(t);
                String token = JwtUtil.createJWT(u.getUserId().toString(), authToken, 24 * 60 * 60 * 1000);
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


    @RequestMapping(value = "jscode2session")
    @ResponseBody
    public AjaxResult jscode2session(
            @RequestParam(value = "code", required = false) String code) {
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code=" + code + "&grant_type=authorization_code";
            System.out.println(url);
            String res = HttpClientUtil.doGet(url);
            Jscode jscode = JsonUtil.fromJson(res, Jscode.class);
            return new AjaxResult(jscode);
        }catch (Exception ex){
            return new AjaxResult(ex.getMessage());
        }
    }
}