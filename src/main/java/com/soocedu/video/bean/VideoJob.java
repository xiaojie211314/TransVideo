package com.soocedu.video.bean;

import lombok.Data;

/**
 *  video工作任务 entity
 * <p>
 * Date:     2017/10/12
 *
 * @author lj
 */
@Data
public class VideoJob {
    private int id;//主键
    private String filename="";//文件名
    private String command="";
    private String srcpath="";//源视频地址(绝对路径)
    private String despath="";//转码视频地址(绝对路径)
    private String srcurl="";//视频源上传地址(相对路径)

    private String desurl="";//视频转码成功后地址(相对路径)


    private int status;//	是	int	视频状态 0:等待转码中 1:转码进行中 2:转码成功 3:转码失败 4:转码成功回调失败
    private int counts;//	是	int	转码次数
    private String msg;//	否	varchar	转码错误信息，如果有错误信息就写入
    private String error;//	否	varchar	回调错误信息，返回接收到回调接口的信息
    private String persistentid;//	是	varchar	视频唯一值,uuid 格式,用来转码成功后告知服务器更新哪个视频
    private long inputtime ;//	是	date	视频插入时间
    private long updatetime;//	是	date	视频更新时间（开始转码，转码成功或转码失败）
    private String fdsdomain="";//fastdfs 主域名
    private String fdsurl="";//fastdfsurl 地址
    private String persistentNotifyUrl;//回调地址

    //附加
    private String sign;//接口安全通信钥匙

    private Voptions voptions;//视频格式参数

}
