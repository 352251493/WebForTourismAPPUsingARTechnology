package com.gxg.utils;

import java.security.MessageDigest;

/**
 * Created by 郭欣光 on 2018/5/29.
 */

public class Md5 {

    /**
     * md5加密
     * @param s 要加密的字符串
     * @return 加密后的32位字符串
     */
    public static String md5 (String s) {
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for(int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(Exception e) {
            return null;
        }
    }
}
