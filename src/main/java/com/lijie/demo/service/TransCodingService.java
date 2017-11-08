package com.lijie.demo.service;

import com.lijie.demo.bean.Const;
import com.lijie.demo.bean.Utils;
import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.bean.VideoUpload;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import com.lijie.demo.task.TransCodeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class TransCodingService {
    private Logger log = LoggerFactory.getLogger(TransCodingService.class);
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Autowired
    private TransCodingMapper transCodingMapper;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;


    private String uploadRootDir;
    private String inDir;
    private String outDir;



    //上传
    public boolean upload(VideoUpload videoUpload) {

        try {
            String infileName = Utils.getUUID()+videoUpload.getFile().getOriginalFilename().substring(videoUpload.getFile().getOriginalFilename().lastIndexOf("."));
            File inFile = new File(uploadRootDir+inDir+infileName);
            log.debug(">>>>>infilename>>"+infileName );
            //复制文件
            videoUpload.getFile().transferTo(inFile);


            VideoJob videoJob = new VideoJob();
            videoJob.setCallUrl(videoUpload.getCallUrl());
            videoJob.setFilename(infileName);
            videoJob.setSrcpath(inFile.getAbsolutePath());
            videoJob.setSrcurl(inDir+infileName);
            videoJob.setStatus(0);
            videoJob.setCounts(0);
            videoJob.setVideokey(videoUpload.getVideokey());


            String outFileName = Utils.getUUID()+".mp4";
            videoJob.setDespath(uploadRootDir+outDir+outFileName);
            videoJob.setDesurl(outDir+outFileName);



            //插入数据库记录
            transCodingMapper.insertJob(videoJob);


            log.debug(">>>插入数据库成功 >>>>videoid: "+videoJob.getId());

            //转码
            taskExecutor.execute(new TransCodeTask(transCodingMapper,videoJob,httpclientUtil));



//            transVideo();

        } catch (IOException e) {
            log.error(e.getMessage());

            return false;
        }


        return true;

    }



    /**
     * 验证签名
     * @param videoUpload
     * @return
     */
    public String validateSign(VideoUpload videoUpload){
        String remotesign = Utils.encode(videoUpload.getSign()+videoUpload.getCallUrl()+videoUpload.getWidth()+videoUpload.getHeight());
        String localsign = Utils.encode(Const.sign_key+videoUpload.getCallUrl()+videoUpload.getWidth()+videoUpload.getHeight());
        if(remotesign.equals(localsign)){
            return localsign;
        }
        return null;
    }


    public List<VideoJob> findListVideos(){

        return transCodingMapper.findAllVideos();
    }

    /**
     * 根据 videokey 查询视频
     * @param videokey
     * @return
     */
    public VideoJob findVideoByVideokey(String videokey){
        return transCodingMapper.findyVideoByVideokey(videokey);
    }

    @Value("${video.in.srcpath}")
    public void setInDir(String inDir) {
        this.inDir = inDir;
    }


    @Value("${video.out.despath}")
    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    @Value("${video.upload.root}")
    public void setUploadRootDir(String uploadRootDir) {
        this.uploadRootDir = uploadRootDir;
    }
}
