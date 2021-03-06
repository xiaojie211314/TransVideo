package com.soocedu.task;

import com.soocedu.fastdfs.FastDFSClient;
import com.soocedu.fastdfs.domain.StorageNodeInfo;
import com.soocedu.fastdfs.domain.StorePath;
import com.soocedu.httpclient.HttpclientUtil;
import com.soocedu.video.bean.VideoCall;
import com.soocedu.video.bean.VideoJob;
import com.soocedu.video.bean.VideoResult;
import com.soocedu.video.dao.TransCodingMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransCodeTask implements Runnable {
    private Logger log = LoggerFactory.getLogger(TransCodeTask.class);

    private TransCodingMapper transCodingMapper;
    private HttpclientUtil httpclientUtil;

    private VideoJob videoJob;


    private FastDFSClient fastDFSClient;


    public TransCodeTask(TransCodingMapper transCodingMapper, VideoJob videoJob, HttpclientUtil httpclientUtil, FastDFSClient fastDFSClient) {


        this.videoJob = videoJob;
        this.transCodingMapper = transCodingMapper;
        this.httpclientUtil = httpclientUtil;
        this.fastDFSClient = fastDFSClient;
    }

    @Override
    public void run() {

        if (transfer()) {
            //1 修改数据库成功状态 , 0 表示转码成功 1 表示正在转码队列中  2 转码进行中  3 表示转码失败

            //是否用 fastdfs
            if(fastDFSClient.isFdfs()) {
                videoJob.setStatus(5);
            }else{
                videoJob.setStatus(0);
            }
            videoJob.setMsg("转码成功");
            transCodingMapper.updateJob(videoJob);
            log.info(">>>>>转码成功 【0】, 转码视频的 persistentid:【" + videoJob.getPersistentid() + "】 ");

            //像客户端发送消息,告知转码成功
        } else {
            // 1 修改数据库转码失败状态
            videoJob.setStatus(3);
            videoJob.setMsg("转码失败");
            videoJob.setError("转码失败");
            transCodingMapper.updateJob(videoJob);
            // 2 告知数据库转码失败.
            log.debug(">>>>>转码执行失败【3】 , 转码视频的 persistentid:【" + videoJob.getPersistentid() + "】 ");


        }

        //回调 php
        callVideo(videoJob);


    }


    //回调
    private void callVideo(VideoJob videoJob) {

        VideoCall videoCall = new VideoCall();


        if (videoJob.getStatus() == 0) {
            videoCall.setError("转码成功");


            //是否用 fastdfs
            if(fastDFSClient.isFdfs()) {

                try {
                    //转码成功调用 fastdfs
                    StorePath storePath = fastDFSClient.uploadFile(new File(videoJob.getDespath()));
                    StorageNodeInfo storageNodeInfo = fastDFSClient.getStroageInfo(storePath);

                    videoJob.setFdsdomain(storageNodeInfo.getIp());
                    videoJob. setFdsurl(storePath.getFullPath());
                    videoJob.setMsg("转码成功 | 上传 fastDFS 成功");
                    videoJob.setStatus(0);
                    videoCall.setError("转码成功 | 上传 fastDFS 成功");

                    log.debug(">>>>>上传 fastdfs 服务：返回 fastdfs 服务地址：【" + storageNodeInfo + "】");
                } catch (Exception e) {

                    videoJob.setMsg("转码成功 | 上传 fastDFS 失败");
                    videoJob.setError("上传 fastDFS 失败");
                    videoJob.setStatus(3);

                    videoCall.setError("转码成功 | 上传 fastDFS 失败");


                }

            }

        }

        videoCall.setCode(videoJob.getStatus());
        videoCall.setId(videoJob.getPersistentid());
        videoCall.setDesc("转码文件名称【" + videoJob.getFilename() + "】| 转码文件路径【" + videoJob.getDesurl() + "】");
        List<VideoResult> videoResults = new ArrayList<>();
        videoResults.add(new VideoResult(videoJob.getDesurl(), videoJob.getFdsdomain(), videoJob.getFdsurl()));
        videoCall.setItems(videoResults);



        //回调
        String result = httpclientUtil.post(videoJob.getPersistentNotifyUrl(), videoCall);

        if (!StringUtils.isEmpty(result)) {
            videoJob.setMsg(videoJob.getMsg().concat(" | 回调成功 "));
            videoJob.setError("回调成功 【 " + result + " 】");

        } else {
            //回调失败
            //如果转码失败的话，回调后不更新状态
            if (videoJob.getStatus() != 3) {
                videoJob.setStatus(4);
                videoJob.setMsg(videoJob.getMsg().concat(" | 回调失败 "));
                videoJob.setError("回调失败 【 " + result + " 】");
            }

        }
        log.info(">>>>>"+videoJob.getMsg()+"【" + videoJob.getStatus() + "】 ,视频的 persistentid:【" + videoJob.getPersistentid() + "】,");

        transCodingMapper.updateJob(videoJob);
    }


    public boolean transfer() {


        /** -i  输入视频地址
         * -c:v libx264 使用h.264 編碼
         * -vcodec libx264 強制指定視頻編碼模式
         * -b bitrate 设置比特率，缺省200kb/s
         * -b:v 264k  视频码率
         * -b:a 音频码率
         * -bufsize 设置码率控制缓冲器的大小
         * - minrate -maxrate 设置 码率的阙值(最大最小值)
         *  -s 分辨率控制
         * -g 关键帧建控制
         * -vf fps=fps=25  帧率 25 或者 -r fps
         * -aspect 16:9  横纵比 16:9  4:3
         * -f mp4 强制转 mp4
         * -y 覆盖输出文件
         *
         * 水印
         * ./ffmpeg -i input.mp4 -i logo.phg -filter_complex overlay output.mp4  左上角
         * overlay=W-w 右上角
         * overlay=0:H-h 左下角
         * overlay=W-w:H-h 右下角
         */


        //正在转码中
        videoJob.setStatus(2);
        videoJob.setMsg("正在转码");

        log.info(">>>>>正在转码【2】 , 转码视频的 persistentid:【" + videoJob.getPersistentid() + "】 ");

        transCodingMapper.updateJob(videoJob);
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(videoJob.getCommand());
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ((line = br.readLine()) != null) {
                log.debug(">>>>>正在转码【2】, 转码视频的 persistentid:【"+videoJob.getPersistentid()+"】 "+line);
//                int exitVal = proc.waitFor();
//                System.out.println("Success Process exitValue: " + exitVal);
            }

            int exitVal = proc.waitFor();
            log.debug("Success Process exitValue: " + exitVal);
            if (exitVal == 0) {
                return true;
            } else {

                //获取转码次数
                int counts = videoJob.getCounts();
                if (counts < 3) {
                    videoJob.setCounts(videoJob.getCounts() + 1);

                    log.info(">>>>>转码失败重新转码状态【2】，转码次数【" + videoJob.getCounts() + "】, 转码视频的 persistentid:【" + videoJob.getPersistentid() + "】 ");
                    transfer();
                }

                videoJob.setMsg("转码命令执行失败！");

                return false;
            }

        } catch (Throwable t) {
            //判断转码次数是否大于三次  如果小于3次 重新转码
            log.warn(">>>>>转码异常..", t.getCause());

            //获取转码次数
            int counts = videoJob.getCounts();
            if (counts < 3) {
                videoJob.setCounts(videoJob.getCounts() + 1);
                log.debug(">>>>>转码失败重新转码状态【2】，转码次数【" + videoJob.getCounts() + "】, 转码视频的 persistentid:【" + videoJob.getPersistentid() + "】 ");

                transfer();
            }
            videoJob.setMsg("抛出异常：" + t.getMessage());
            return false;
        }
    }


}


