package cn.xtits.xtp.enums;

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
