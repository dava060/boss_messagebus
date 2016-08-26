package com.moretv.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/15.
 */
public class TimeUtils {
    // 将字符串转为时间戳
    public static String getTimestamp(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time =str.substring(0, 10);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
        // 例如：cc_time=1291778220
        long lcc_time =Long.valueOf(cc_time);
        re_StrTime =sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    public static void main(String[] args) {
        //String time = "2016年08月15日11时17分00秒";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        System.out.println(str); // 字符串=======>时间戳
        String re_str =getTimestamp(str);
        System.out.println(re_str);  //时间戳======>字符串
        String data =getStrTime(re_str);
        System.out.println(data);
    }
}
