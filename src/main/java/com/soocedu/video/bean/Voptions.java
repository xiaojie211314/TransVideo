package com.soocedu.video.bean;

import lombok.Data;

/**
 * 视频的格式
 * Created by jieli on 2017/11/14.
 */
@Data
public class Voptions {
    private String avthumb;//转换格式
    private String ab;//声音码率
    private String acodec;//音频格式
    private String r;//帧率
    private String vb;//视频码率
    private String vcodec;//视频格式 libx264
    private String s;//分辨率
    private String ar;//设置音频采样率
}
