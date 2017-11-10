package com.soocedu.video.controller;/**
 * Created by Administrator on 2017/6/19 0019.
 */

import com.soocedu.video.bean.VideoJob;
import com.soocedu.video.bean.VideoResult;
import com.soocedu.video.bean.Videos;
import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.video.service.TransCodingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 2017/6/19 0019
 * Time: 下午 8:45
 */
@CrossOrigin
@Controller
@RequestMapping("video")
public class TransCodingController {
    private Logger log = LoggerFactory.getLogger(TransCodingController.class);
    @Autowired
    private HttpclientUtil httpclientUtil;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Autowired
    private TransCodingService transCodingService;




    @RequestMapping("upload")
    @ResponseBody
    public VideoResult upload(MultipartFile file, @RequestParam("token") String token) {


        return transCodingService.upload(file,token);

    }

    @RequestMapping("getVideo")
    @ResponseBody
    public VideoJob getVideo(String videokey){

        return  transCodingService.findVideoByVideokey(videokey);
    }
    @RequestMapping("look")
    @ResponseBody
    public List<Videos> success(){

        List<Videos>  videoJobList = transCodingService.findVideos();
//        ModelAndView modelAndView = new ModelAndView("success");
//
//        //将数据放到request中
//        modelAndView.addObject("dataList", videoJobList);

        return videoJobList;
    }
}
