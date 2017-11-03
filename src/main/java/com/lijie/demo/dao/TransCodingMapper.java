package com.lijie.demo.dao;


import com.lijie.demo.bean.VideoJob;

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


}
