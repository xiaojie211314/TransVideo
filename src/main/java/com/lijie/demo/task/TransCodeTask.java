package com.lijie.demo.task;

import com.lijie.demo.bean.VideoJob;
import com.lijie.demo.dao.TransCodingMapper;
import com.lijie.demo.httpclient.HttpclientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TransCodeTask implements Runnable {
    private Logger log = LoggerFactory.getLogger(TransCodeTask.class);

    private TransCodingMapper transCodingMapper;
    private HttpclientUtil httpclientUtil;

    private VideoJob videoJob;



    public TransCodeTask( TransCodingMapper transCodingMapper,VideoJob videoJob, HttpclientUtil httpclientUtil) {



        this.videoJob = videoJob;
        this.transCodingMapper = transCodingMapper;
        this.httpclientUtil = httpclientUtil;
    }

    @Override
    public void run() {

        if(transfer(videoJob.getSrcpath(), videoJob.getDespath())){
            //1 修改数据库成功状态 0 表示正在转码队列中, 1 表示转码成功  2 表示转码失败
            log.debug("转码成功,修改数据库状态为>>>> 1 .... ");

            videoJob.setStatus(2);
            videoJob.setMsg("转码成功");
            transCodingMapper.updateJob(videoJob);
            //像客户端发送消息,告知转码成功
            log.debug("通知客户端,转码成功 .....");
        } else{
            // 1 修改数据库转码失败状态
            log.debug("转码失败,修改数据库状态为>>>> 2 .... ");
            videoJob.setStatus(3);
            transCodingMapper.updateJob(videoJob);
            // 2 告知数据库转码失败.
            log.debug("通知客户端,转码失败 ....");
            //通知失败如何处理



        }

       String result = httpclientUtil.post(videoJob.getCallUrl(),videoJob);

        if(result!=null){
            videoJob.setCallmsg("回调服务器成功！");
            transCodingMapper.updateJob(videoJob);
        }else {
            videoJob.setStatus(4);
            videoJob.setCallmsg("回调服务器失败！");
            transCodingMapper.updateJob(videoJob);
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


        String transferMp4 = "ffmpeg -i  " + infile + " -s "+ videoJob.getWidth()+"x"+videoJob.getHeight()+"  -c:v libx264  -g 10  -b:v 512k -bufsize 512k  -b:a 64k -r 30 -aspect 16:9  -f mp4 -y " + outfile;


        //正在转码中
        videoJob.setStatus(1);

        videoJob.setCounts(videoJob.getCounts()+1);

        videoJob.setCommand(transferMp4);

        transCodingMapper.updateJob(videoJob);
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(transferMp4);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ( (line = br.readLine()) != null) {
              log.debug(line);
//                int exitVal = proc.waitFor();
//                System.out.println("Success Process exitValue: " + exitVal);
            }

            int exitVal = proc.waitFor();
            log.debug("Success Process exitValue: " + exitVal);
            if(exitVal==0){
                return true;
            }else {
               log.debug("转码失败,重新转码....");
                //获取转码次数
                int counts = videoJob.getCounts();
                if(counts<3) {
//                    videoJob.setCounts(videoJob.getCounts() + 1);
                    transfer(videoJob.getSrcpath(), videoJob.getDespath());
                }

                videoJob.setMsg("转码命令执行失败！");

                return false;
            }

        } catch (Throwable t) {
            //判断转码次数是否大于三次  如果小于3次 重新转码
           log.error("转码失败,重新转码....",t.getCause());

            //获取转码次数
            int counts = videoJob.getCounts();
            if(counts<3) {
//                    videoJob.setCounts(videoJob.getCounts() + 1);
                transfer(videoJob.getSrcpath(), videoJob.getDespath());
            }
            videoJob.setMsg("抛出异常："+t.getMessage());
            return false;
        }
    }



}


