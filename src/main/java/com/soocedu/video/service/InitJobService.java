package com.soocedu.video.service;

import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.task.TransCodeTask;
import com.soocedu.video.bean.VideoDir;
import com.soocedu.video.bean.VideoJob;
import com.soocedu.video.dao.TransCodingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 服务第一次启动初始化
 */
@Service
public class InitJobService {
    private Logger log = LoggerFactory.getLogger(InitJobService.class);

    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;


    @Autowired
    private VideoDir videoDir;

    @PostConstruct
    public void init(){

        log.info(">>>>>>>>>>查询数据库任务,写到内存....");

        //执行数据库写入内存
        List<VideoJob> videoJobList = transCodingMapper.findJobTask();

        if(null != videoJobList && !videoJobList.isEmpty())
            for (VideoJob videoJob: videoJobList){

                videoJob.setStatus(1);

                taskExecutor.execute(new TransCodeTask(transCodingMapper,videoJob,httpclientUtil));
            }
    }

}
