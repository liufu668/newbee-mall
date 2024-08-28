package com.study.newbeeMall.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SystemUtil {

    private SystemUtil(){

    }

    /**
     * 登录或注册成功后,生成保持用户登录状态会话token值
     */
    public static String genToken(String src){
        if(null == src || "".equals(src)){
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString();
            if(result.length() == 31){
                result = result + "-";
            }
            System.out.println("token值为: "+result);
            return result;

        } catch (Exception e) {
            return null;
        }
    }
}
