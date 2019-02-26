package cn.xtits.xtp.util;

public class CommonUtil {

    public static String getSplitCode(int num) {

        if (num < 10) {

            return "0" + num;
        }
        return num + "";
    }
}
