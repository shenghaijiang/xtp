package cn.xtits.xtp.enums;

/**
 * Created by ShengHaiJiang on 2017/3/27.
 */
public enum ErrorCodeEnums {

    NO_ERROR(1, "成功"),
    NORMAL_ERROR(0, "逻辑错误"),
    SERVER_ERROR(-1, "记录已存在"),
    RECORD_EXISTS(-2, "记录已存在");

    ErrorCodeEnums(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int value;
    public String msg;
}
