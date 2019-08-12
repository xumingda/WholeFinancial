package com.ciba.wholefinancial.util;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/4/20 0020.
 */
public class MD5Utils {
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] btInput = s.getBytes("UTF-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @category 签名
     * @param app_id
     * @param app_secret
     * @param timetemp
     * @return
     */
    public static String sign(String app_id, String app_secret, String timetemp) {
        String sign = app_id + app_secret + timetemp;
        sign = MD5Utils.MD5(sign).toLowerCase();
        return sign;
    }
}
