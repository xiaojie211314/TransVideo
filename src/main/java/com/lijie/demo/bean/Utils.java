package com.lijie.demo.bean;

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

    public static void main( String[] args){
        System.out.print("asds送死森色扥森达");
    }
}
