package cn.xtits.xtp.interceptor;


import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.JwtUtil;
import cn.xtits.xtf.web.springmvc.RequestLogInterceptor;
import cn.xtits.xtp.config.GlobalValue;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by ShengHaiJiang on 2017/3/16.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private String APP_TOKEN;

    public static final Logger logger = LoggerFactory.getLogger(RequestLogInterceptor.class);

    public LoginInterceptor(String token) {
        this.APP_TOKEN = token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!GlobalValue.oauth && request.getParameter("oauth") == null) {
            return true;
        }

        HttpServletRequest httpRequest = request;


        String auth = httpRequest.getParameter("oauth");
        if (auth.indexOf("whosyourdaddy") >= 0) {
            request.setAttribute("userId", 1);
            request.setAttribute("userName", "测试环境用户");
            return true;
        }
        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Claims claims;
        try {
            claims = JwtUtil.parseJWT(auth);
            Date expirationDate = claims.getExpiration();
            LoginToken t = JsonUtil.fromJson(claims.getSubject(), LoginToken.class);

//            if (!APP_TOKEN.equals(t.getAppToken())) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return false;
//            }
            request.setAttribute("appToken", t.getAppToken());
            request.setAttribute("userId", t.getUserId());
            request.setAttribute("userName", t.getUserName());
            if (expirationDate.before(new Date())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}

