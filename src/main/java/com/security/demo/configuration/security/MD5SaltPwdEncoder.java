package com.security.demo.configuration.security;

import com.security.demo.util.Md5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author coco
 * @date 2020-09-06 15:16
 **/
public class MD5SaltPwdEncoder implements PasswordEncoder {

    //private static final String salt = "safety_supervision";

    private static final String salt = "wisdom_court";

    public MD5SaltPwdEncoder() {
    }

    @Override
    public String encode(CharSequence charSequence) {
        String s = salt + charSequence.toString();
        String newpwd = Md5Util.encode(s).toUpperCase();
        return newpwd;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String encPass = salt + charSequence.toString();
        return Md5Util.isPasswordValid(s.toLowerCase(),encPass);
    }
}