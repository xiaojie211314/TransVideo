package com.soocedu.video.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 视频回调 model
 * Created by jieli on 2017/11/9.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoCall extends  BaseBean{
    private String id;//视频唯一值

    private String desc;//视频转码说明

    private List<VideoResult> items;


}
