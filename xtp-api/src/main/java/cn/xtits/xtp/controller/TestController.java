package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.service.LogService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private LogService service;

    @RequestMapping(value = "nothing")
    @ResponseBody
    public AjaxResult nothing() {
        String key = "jwt:" + String.valueOf(System.currentTimeMillis());
        String token="The software provides a freely available application programming interface (API) called Steamworks, which developers can use to integrate many of Steam's functions into their products, including networking, matchmaking, in-game achievements, microtransactions, and support for user-created content through Steam Workshop. Though initially developed for use on Microsoft Windows operating systems, versions for macOS and Linux were later released. Mobile apps with connected functionality with the main software were later released for iOS, Android, and Windows Phone in the 2010s. The platform also offers a small selection of non-video game content, such as design software, anime, and films.The Steam platform is the largest digital distribution platform for PC gaming, estimated in 2013 to have 75% of the market space.[4] By 2017, users purchasing games through Steam totaled roughly US$4.3 billion, representing at least 18% of global PC game sales.[5] By early 2018, the service had over 150 million registered accounts with a peak of 18.5 million concurrent users online. The success of the Steam platform has led to the development of a line of Steam Machine microconsoles, which include the SteamOS operating system and Steam Controllers, and various virtual reality devices";
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, token, 60 * 24, TimeUnit.MINUTES);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }
}