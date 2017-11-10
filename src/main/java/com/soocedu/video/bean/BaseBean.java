package com.soocedu.video.bean;

import lombok.Data;

/**
 * Created by jieli on 2017/10/26.
 */
@Data
public class BaseBean {
    private int code = 0;//状态码 0 成功 2 逻辑错误 1 服务器维护
    private String  error = "";// 接口信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
