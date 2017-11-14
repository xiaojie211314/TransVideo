package com.soocedu.video.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件 model
 * Created by jieli on 2017/11/9.
 */
@Data
public class UploadFile {
    private MultipartFile file;//上传文件流
    private String persistentOps; //视频配置参数
    private String persistentNotifyUrl;//转码成功后回调
    private String outkey;//文件转码生成的文件名
    private Voptions voptions;//视频转码参数


}
