package cn.xtits.xtp.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by ShengHaiJiang on 2017/3/16.
 */
public class BaseController {
    public Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return Integer.parseInt(request.getAttribute("userId").toString());
    }

    public String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getAttribute("userName").toString();
    }

    public String getAppToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getAttribute("appToken").toString();
    }

    public Date getDateNow() {
        return new Date();
    }
}
