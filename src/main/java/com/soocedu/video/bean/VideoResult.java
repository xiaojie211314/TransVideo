package com.soocedu.video.bean;

import com.soocedu.util.Uuids;
import lombok.Data;

/**
 * Created by jieli on 2017/10/26.
 */
@Data
public class VideoResult extends BaseBean {
    private String key="";//上传返回文件相对路径
    private String hash = Uuids.getUUID();// hash值
    private String persistentId = Uuids.getUUID();//视频唯一识别 key

    public VideoResult(String key){
        this.key = key;
    }

    public VideoResult(){
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }
}
