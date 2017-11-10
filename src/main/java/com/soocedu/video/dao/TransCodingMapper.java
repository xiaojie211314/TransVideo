package com.soocedu.video.dao;


import com.soocedu.video.bean.VideoJob;
import com.soocedu.video.bean.VideoLook;

import java.util.List;

public interface TransCodingMapper {

    /**
     * 插入任务
     * @param videoJob
     */
    void insertJob(VideoJob videoJob);

    /**
     * 更新任务状态 0 未执行 1 成功 2 失败
     * @param videoJob
     */
    void updateJob(VideoJob videoJob);

    /**
     * 查询未执行的任务
     * @return
     */
    List<VideoJob> findJobTask();


    List<VideoJob> findAllVideos();

    List<VideoLook> findVideos();


    /**
     * 根据 videokey 查询视频
     * @param videokey
     * @return
     */
    VideoJob findyVideoByVideokey(String videokey);


}
