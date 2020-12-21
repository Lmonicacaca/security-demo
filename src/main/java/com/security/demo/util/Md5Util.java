package com.security.demo.util;

import com.security.demo.configuration.security.MD5SaltPwdEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util { private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private Md5Util() {
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for(int var4 = 0; i < l; ++i) {
            out[var4++] = DIGITS[(240 & data[i]) >>> 4];
            out[var4++] = DIGITS[15 & data[i]];
        }

        return out;
    }

    public static String encode(String rawPass) {
        MessageDigest messageDigest = getMessageDigest();
        byte[] digest = messageDigest.digest(rawPass.getBytes());
        return (new String(encodeHex(digest))).toLowerCase();
    }

    protected static final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            throw new IllegalArgumentException("No such algorithm [MD5]");
        }
    }

    public static boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass);
        return pass1.equals(pass2);
    }
    public static void main(String[] args) {
        String result = encode("123456");
        System.out.println("password=" + result.toUpperCase());
        boolean isGood = isPasswordValid("EFB7C11129052F8421373FABA50C94E8", "123456");
        System.out.println("isGood=" + isGood);
    }

}
