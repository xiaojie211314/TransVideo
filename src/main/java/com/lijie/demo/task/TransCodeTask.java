package com.lijie.demo.task;

import com.lijie.demo.bean.Const;
import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class TransCodeTask implements Runnable {

    private TransCodingMapper transCodingMapper;
    private HttpclientUtil httpclientUtil;
    private String srcpath;
    private String despath;
    private VideoJob videoJob;



    public TransCodeTask( TransCodingMapper transCodingMapper,VideoJob videoJob, HttpclientUtil httpclientUtil,String outDir,String uploadRootDir) {

        String fileOutName = new Date().getTime()+".mp4";

        //设置转码地址
        videoJob.setDespath(outDir+fileOutName);

        this.videoJob = videoJob;
        this.transCodingMapper = transCodingMapper;
        this.srcpath = uploadRootDir+videoJob.getSrcpath();
        this.despath = uploadRootDir+videoJob.getDespath();
        this.httpclientUtil = httpclientUtil;
    }

    @Override
    public void run() {
        if(transfer(srcpath, despath)){
            //1 修改数据库成功状态 0 表示正在转码队列中, 1 表示转码成功  2 表示转码失败
            System.out.println("转码成功,修改数据库状态为>>>> 1 .... ");

            videoJob.setStatus("1");
            transCodingMapper.updateJob(videoJob);
            //像客户端发送消息,告知转码成功
            System.out.println("通知客户端,转码成功 .....");
             httpclientUtil.requestTest();
        } else{
            // 1 修改数据库转码失败状态
            System.out.println("转码失败,修改数据库状态为>>>> 2 .... ");
            videoJob.setStatus("2");
            transCodingMapper.updateJob(videoJob);
            // 2 告知数据库转码失败.

            System.out.println("通知客户端,转码失败 ...更新转码次数..");
            //通知失败如何处理
            httpclientUtil.requestTest();


        }

    }




    public boolean transfer(String infile,String outfile) {
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


        String transferMp4 = "ffmpeg -i  " + infile + "  -c:v libx264  -g 10  -b:v 512k -bufsize 512k  -b:a 64k -r 30 -aspect 16:9  -f mp4 -y " + outfile;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(transferMp4);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null) {
                System.out.println("转码中:" + line);
//                int exitVal = proc.waitFor();
//                System.out.println("Success Process exitValue: " + exitVal);
            }

            int exitVal = proc.waitFor();
            System.out.println("Success Process exitValue: " + exitVal);
            if(exitVal==0){
                return true;
            }else {
                return false;
            }

        } catch (Throwable t) {
            //判断转码次数是否大于三次  如果小于3次 重新转码
            System.out.println("转码失败,重新转码....");
//            transfer(srcpath, despath);
            return false;
        }
    }



}


