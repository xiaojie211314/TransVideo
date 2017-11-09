package com.lijie.demo.bean;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Token;

/**
 * Created by jieli on 2017/10/23.
 */
public class test {

    public static void main( String[] args){
        String tokenkey = "HC5_GylOBVm8AY_XXFDD5Lkk0MiW7p2tjPG9_1o1:8UKf90eYcHJLBB4Gz5TIan5jeqU=:eyJzY29wZSI6InRlc3QiLCJkZWFkbGluZSI6MTUxMDE5OTY5MiwiZnNpemVMaW1pdCI6MTcxNzk4NjkxODR9";

        String sk = "FXeoUwFhWShmpttTdNx74YIf1-tS-6O6enetIySN";
        Token token = new Token(tokenkey,sk);



        //转换成 json 对象

        JSONObject jsonObject = token.getPutPolicy();

        System.out.println( "获取 policy json 数据 "+jsonObject.toJSONString());

        System.out.println("json scope 值："+ jsonObject.getString("scope"));



        String ak = "HC5_GylOBVm8AY_XXFDD5Lkk0MiW7p2tjPG9_1o1";

        System.out.println(token.isValidCallback(ak));
    }
}
