package com.soocedu.video.bean;

import com.soocedu.util.Uuids;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jieli on 2017/10/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoResult extends BaseBean {
    private String key="";//上传返回文件相对路径
    private String hash = Uuids.getUUID();// hash值
    private String persistentId;//视频唯一识别 key
    private String fdsDomain;//fastDFS 域名]
    private String fdsUrl;//fastDFS 地址

    /**
     * 构造函数
     * @param key
     * @param fdsDomain
     * @param fdsUrl
     */
    public VideoResult(String key, String fdsDomain, String fdsUrl){
        this.key = key;
        this.fdsDomain = fdsDomain;
        this.fdsUrl = fdsUrl;
    }

    public VideoResult(){
    }

}
