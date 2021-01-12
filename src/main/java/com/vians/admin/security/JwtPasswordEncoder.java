package com.vians.admin.security;

import com.vians.admin.utils.DoNetUnicodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密算法
 * Created by wangkun23 on 2019/1/16.
 */
public class JwtPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        /**
         * 一定要注意.net或java的MD5字节是不一样的,所以需要转换一下
         */
        byte[] passPhrase = DoNetUnicodeUtils.toUnicodeMC(charSequence.toString());
        return DigestUtils.md5Hex(passPhrase).toUpperCase();
    }

    @Override
    public boolean matches(CharSequence charSequence, String encPass) {
        String password = encode(charSequence);
        if (password.equals(encPass)) {
            return true;
        }
        return false;
    }
}
