package com.lijie.demo.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jieli on 2017/11/2.
 */
public class Utils {
    public static String getCurrTimestamp(){
        return new Date().getTime()+"";
    }

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }



    private static final String LOG_TAG = "MD5";
    private static final String ALGORITHM = "MD5";

    private static char sHexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static MessageDigest sDigest;

    static {
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
//            Log.e(LOG_TAG, "Get MD5 Digest failed.");
//			throw new UnsupportedDigestAlgorithmException(ALGORITHM, e);
        }
    }




    final public static String encode(String source) {
        byte[] btyes = source.getBytes();
        byte[] encodedBytes = sDigest.digest(btyes);

        return hexString(encodedBytes);
    }

    public static String hexString(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];
            str[index++] = sHexDigits[b & 0xf];
        }
        return new String(str);
    }



    public static void main( String[] args){
        System.out.print("asds送死森色扥森达");
    }
}
