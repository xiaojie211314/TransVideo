package com.soocedu.video.service;

import com.alibaba.fastjson.JSON;
import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.task.TransCodeTask;
import com.soocedu.util.Const;
import com.soocedu.util.Token;
import com.soocedu.util.Uuids;
import com.soocedu.video.bean.*;
import com.soocedu.video.dao.TransCodingMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
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
    private String imgInDir;
    private String videoInDir;
    private String outDir;


    //上传
    public VideoResult upload(MultipartFile file, @RequestParam("token") String token) {

        VideoResult uploadResult = new VideoResult();
        try {


            Token tokenUtil = new Token(token, Const.SECRET_KEY);

            //验证签名
//            if(!tokenUtil.isValidCallback(Const.ACCESS_KEY)){
//                uploadResult.setCode(2);
//                uploadResult.setError("验证签名失败");
//                return uploadResult;
//            }


            //签名通过，上传文件， 解析 token数据

            UploadFile uploadFile = JSON.parseObject(tokenUtil.getPutPolicy(), UploadFile.class);

            uploadFile.setFile(file);


            //获取上传后缀名
            String infileName = Uuids.getUUID() + uploadFile.getFile().getOriginalFilename().substring(uploadFile.getFile().getOriginalFilename().lastIndexOf("."));


            //图片上传 名称
            if (StringUtils.isEmpty(uploadFile.getPersistentOps())) {

                infileName = imgInDir + infileName;
            } else {
                //视频上传相对路径
                infileName = videoInDir + infileName;
            }


            File inFile = new File(uploadRootDir + infileName);


            log.debug(">>>>>infilename>>" + inFile.getAbsolutePath());
//            //复制文件
            uploadFile.getFile().transferTo(inFile);

            //设置上传文件相对路径
            uploadResult.setKey(infileName);

            //上传文档图片成功
            if (StringUtils.isEmpty(uploadFile.getPersistentOps())) {
                uploadResult.setCode(0);
                uploadResult.setError("文件上传成功");

                return uploadResult;
            }


            //设置视频的唯一 key
            uploadResult.setPersistentId(Uuids.getUUID());

            //上传视频
            VideoJob videoJob = new VideoJob();
            videoJob.setPersistentNotifyUrl(uploadFile.getPersistentNotifyUrl());
            videoJob.setFilename(uploadFile.getOutkey());
            videoJob.setSrcpath(inFile.getAbsolutePath());
            videoJob.setSrcurl(uploadResult.getKey());
            videoJob.setStatus(1);//等待转码
            videoJob.setCounts(1);
            videoJob.setPersistentid(uploadResult.getPersistentId());
//

            videoJob.setDespath(uploadRootDir + outDir + uploadFile.getOutkey());
            videoJob.setDesurl(outDir + uploadFile.getOutkey());

//
//
//            //插入数据库记录
            transCodingMapper.insertJob(videoJob);
//
//
            log.debug(">>>插入数据库成功 >>>>videoid: " + videoJob.getId());
//
//            //转码
            taskExecutor.execute(new TransCodeTask(transCodingMapper, videoJob, httpclientUtil));
//
//
//
////            transVideo();
//
        } catch (Exception e) {
            log.error(e.getMessage());

            uploadResult.setCode(2);
            uploadResult.setError("上传文件失败");
            return uploadResult;
        }


        uploadResult.setCode(0);
        uploadResult.setError("上传文件成功");
        return uploadResult;

    }



    public List<VideoLook> findVideos() {
        return transCodingMapper.findVideos();
    }

    /**
     * 根据 persistentId 查询视频
     *
     * @param persistentId
     * @return
     */

    public VideoCall findVideoByPersistentId(String persistentId) {
        VideoJob videoJob = transCodingMapper.findVideoByPersistentId(persistentId);
        VideoCall videoCall = new VideoCall();
        videoCall.setCode(videoJob.getStatus());
        videoCall.setError(videoJob.getError());
        videoCall.setId(videoJob.getPersistentid());
        videoCall.setDesc(videoJob.getMsg());
        List<VideoResult> listItems = new ArrayList<>();
        listItems.add(new VideoResult(videoJob.getDesurl()));
        videoCall.setItems(listItems);

        return videoCall;
    }

    @Value("${img.in.srcpath}")
    public void setImgInDir(String imgInDir) {
        this.imgInDir = imgInDir;
    }

    @Value("${video.in.srcpath}")
    public void setVideoInDir(String videoInDir) {
        this.videoInDir = videoInDir;
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
