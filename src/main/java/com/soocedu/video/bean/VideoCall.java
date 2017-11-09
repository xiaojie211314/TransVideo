package com.soocedu.video.bean;

/**
 * 视频回调 model
 * Created by jieli on 2017/11/9.
 */
public class VideoCall extends  BaseBean{
    private String id;//视频唯一值

    private String desc;//视频转码说明

    private VideoResult items;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public VideoResult getItems() {
        return items;
    }

    public void setItems(VideoResult items) {
        this.items = items;
    }
}
