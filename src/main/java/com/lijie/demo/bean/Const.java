package com.lijie.demo.bean;

/**
 * Created by jieli on 2017/10/17.
 */
public interface Const {
    public final static String  WEBROOT= System.getProperty("webapp.root");

    public final static  String  sign_key = "83e89d7d87aec36ca1f4149fffe9b8ec";


    /**
     * 成功
     */
    public final static int status0=0;//成功
    /**
     * 服务器问题
     */
    public final static int status1=1;//服务器问题
    /**
     * 逻辑错误
     */
    public final static int status2=2;//逻辑错误
}
