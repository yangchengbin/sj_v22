package cn.careerforce.sj.utils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-11-3
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    public static String getCurTime() {
        return new Date().toString();
    }

    public static long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

}
