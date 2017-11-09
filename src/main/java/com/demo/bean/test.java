package com.demo.bean;

import com.soocedu.util.Token;

/**
 * Created by jieli on 2017/10/23.
 */
public class test {

    public static void main( String[] args){
        String tokenkey = "HC5_GylOBVm8AY_XXFDD5Lkk0MiW7p2tjPG9_1o1:BJhlXhH59GuowU0hGxqGvc8TgJs=:eyJzY29wZSI6InRlc3QiLCJkZWFkbGluZSI6MTUxMDIwOTE3MCwicGVyc2lzdGVudE9wcyI6ImF2dGh1bWJcL20zdThcL3NlZ3RpbWVcLzE1XC9hYlwvMTYwa1wvYXJcLzQ0MTAwXC9hY29kZWNcL2xpYmZhYWNcL3JcLzMwXC92YlwvMjQwMGtcL3Zjb2RlY1wvbGlieDI2NFwvc1wvNjQweDQ4MFwvbm9Eb21haW5cLzF8c2F2ZWFzXC9kR1Z6ZERveU1ERTNNVEV3T1Y5eGJqVmhNRE5sT0RneU5HRTFOalZmTUM1dE0zVTQ7YXZ0aHVtYlwvbTN1OFwvc2VndGltZVwvMTVcL2FiXC8xNjBrXC9hclwvNDQxMDBcL2Fjb2RlY1wvbGliZmFhY1wvclwvMzBcL3ZiXC8yNDAwa1wvdmNvZGVjXC9saWJ4MjY0XC9zXC8xMDgweDcyMFwvbm9Eb21haW5cLzF8c2F2ZWFzXC9kR1Z6ZERveU1ERTNNVEV3T1Y5eGJqVmhNRE5sT0RneU5HRTFOalZmTVM1dE0zVTQiLCJwZXJzaXN0ZW50UGlwZWxpbmUiOiJ0ZXN0IiwicGVyc2lzdGVudE5vdGlmeVVybCI6Imh0dHA6XC9cL3Rlc3R5LnNvb2MuY29tXC9pbmRleC5waHA_cz1hcGlcL1YxXC9GaWxlXC9wZXJzaXN0ZW50Tm90aWZ5VXJsIiwiZnNpemVMaW1pdCI6MTcxNzk4NjkxODR9";

        String sk = "FXeoUwFhWShmpttTdNx74YIf1-tS-6O6enetIySN";
        Token token = new Token(tokenkey,sk);



        //转换成 json 对象

        String jsonObject = token.getPutPolicy();

        System.out.println( "获取 policy json 数据 "+jsonObject);




        String ak = "HC5_GylOBVm8AY_XXFDD5Lkk0MiW7p2tjPG9_1o1";

        System.out.println(token.isValidCallback(ak));


    }
}
