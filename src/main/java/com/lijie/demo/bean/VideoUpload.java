package com.lijie.demo.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Created by jieli on 2017/10/26.
 */

@Data
public class VideoUpload {
    private MultipartFile file;
    private String callUrl;//转码成功后回调地址
    private String sign;//接口安全通信钥匙
    private String width = "1080";//分辨率宽度 默认1024
    private String height = "720";// 分辨率高度,默认768

    private String videokey= UUID.randomUUID().toString();//视频唯一标识符

    public String getCallUrl() {
        return callUrl;
    }

    public void setCallUrl(String callUrl) {
        this.callUrl = callUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public  String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }
}
