package cn.xtits.xtp.enums;

import cn.xtits.xtf.common.web.AjaxResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HaiL on 2017/5/11.
 */
public enum DictTypeEnums {


    DataRule("DataRule", "数据权限");

    DictTypeEnums(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String value;
    public String msg;
}
