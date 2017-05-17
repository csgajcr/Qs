package com.jcrspace.common.manager;

import com.blankj.utilcode.utils.EncryptUtils;
import com.blankj.utilcode.utils.PhoneUtils;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class TokenManager extends BaseManager {

    /**
     * token计算规则 IMEI+用户名 再 md5
     *
     * @param userName
     * @return
     */
    public static String calcToken(String userName){
        String IMEI = PhoneUtils.getIMEI();
        String token = EncryptUtils.encryptMD5ToString(IMEI+userName);
        return token;
    }

    public boolean verifyToken(String token,String userName){
        if (token.equals(calcToken(userName))){
            return true;
        } else {
            return false;
        }
    }


}
