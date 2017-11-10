package com.soocedu.util;

/**
 * Created by jieli on 2017/10/17.
 */
public interface Const {
    public final static String  WEBROOT= System.getProperty("webapp.video.root");

    public final static  String  SECRET_KEY = "FXeoUwFhWShmpttTdNx74YIf1-tS-6O6enetIySN";
    public final static  String  ACCESS_KEY = "HC5_GylOBVm8AY_XXFDD5Lkk0MiW7p2tjPG9_1o1";



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
