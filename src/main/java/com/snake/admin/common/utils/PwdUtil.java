package com.snake.admin.common.utils;

import cn.hutool.core.util.StrUtil;
import com.snake.admin.common.security.sm3.SM3Util;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;

/**
 * 密码工具
 */
public class PwdUtil{

   final static String FORMAT_TEXT = "%s:%s:%s";

    public static String ciphertext(String username,String rawPwd){
        BizAssert.isTrue("username is missing", StrUtil.isBlank(username));
        BizAssert.isTrue("rawPwd is missing", StrUtil.isBlank(rawPwd));
        String format = String.format(username, rawPwd, username);
        return SM3Util.encryptText(format);
    }

    public static Boolean verify(String username,String rawPwd,String ciphertext){
        BizAssert.isTrue("username is missing", StrUtil.isBlank(username));
        BizAssert.isTrue("rawPwd is missing", StrUtil.isBlank(rawPwd));
        String pwd = ciphertext(username, rawPwd);
        return pwd.equals(ciphertext);
    }
}
