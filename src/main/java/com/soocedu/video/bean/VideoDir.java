package com.soocedu.video.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by jieli on 2017/11/13.
 */
@Component("VideoDir")
public class VideoDir {

    private String uploadRootDir;//上传根目录

    private String imgInDir;//图片路径

    private String videoInDir;//视频路径

    private String outDir;//视频转码路径

    public String getUploadRootDir() {
        return uploadRootDir;
    }

    public String getImgInDir() {

        return imgInDir;
    }


    public String getVideoInDir() {
        return videoInDir;
    }

    public String getOutDir() {
        return outDir;
    }
    @Value("${video.upload.root}")
    public void setUploadRootDir(String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
    }

    @Value("${img.in.srcpath}")
    public void setImgInDir(String imgInDir) {
        //图片上传目录
        File file = new File(uploadRootDir + imgInDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.imgInDir = imgInDir;
    }

    @Value("${video.in.srcpath}")
    public void setVideoInDir(String videoInDir) {
        //视频上传目录
        File file = new File(uploadRootDir + videoInDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.videoInDir = videoInDir;
    }

    @Value("${video.out.despath}")
    public void setOutDir(String outDir) {
        //转码目录
        File file = new File(uploadRootDir + outDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.outDir = outDir;
    }




}
