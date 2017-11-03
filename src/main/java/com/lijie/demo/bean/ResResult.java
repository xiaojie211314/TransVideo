package com.lijie.demo.bean;

/**
 * Created by jieli on 2017/10/26.
 */
public class ResResult extends BaseResult {
    private String videokey="";//视频唯一标示 uuid;
    private String sign=""; //接口安全通信钥匙

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
