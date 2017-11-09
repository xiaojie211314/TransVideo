package com.soocedu.util;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jieli on 2017/11/2.
 */
public class Uuids {
    public static String getCurrTimestamp(){
        return new Date().getTime()+"";
    }

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }


    public static void main( String[] args){
        System.out.print("");
    }
}
