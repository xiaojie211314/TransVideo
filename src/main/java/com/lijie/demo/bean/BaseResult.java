package com.lijie.demo.bean;

/**
 * Created by jieli on 2017/10/26.
 */
public class BaseResult {
    private int status = 0;//状态码 0 成功 2 逻辑错误 1 服务器维护
    private String  statusCode = "成功!";// 接口信息

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
