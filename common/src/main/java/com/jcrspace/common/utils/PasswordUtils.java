package com.jcrspace.common.utils;

import com.blankj.utilcode.utils.EncryptUtils;
import com.blankj.utilcode.utils.Utils;

import org.xutils.common.util.MD5;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class PasswordUtils {
    private static final String ENCRYPT_KEY = "QS";

    public static String convert(String src){
        String finalPassword = ENCRYPT_KEY + src + ENCRYPT_KEY;
        return EncryptUtils.encryptMD5ToString(EncryptUtils.encryptMD5ToString(finalPassword));
    }

    public static boolean verify(String src,String dest){
        if (convert(src).equals(dest)){
            return true;
        } else {
            return false;
        }
    }

}
