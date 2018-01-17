package com.soocedu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jieli on 2017/11/2.
 */
public class Token {

    private Logger log = LoggerFactory.getLogger(Token.class);

    private String accessKey;
    private String secretKey;
    private String encodedSign;
    private String encodedPutPolicy;

    /**
     * 实例化 token
     * @param token 加密算法数据
     * @param secretkey  秘钥
     */
    public Token(String token, String secretkey) {
        this.secretKey = secretkey;
        String[] result = token.split(":");
        this.accessKey = result[0];
        this.encodedSign = result[1];
        this.encodedPutPolicy = result[2];
    }

    /**
     * 得到token里面保存的json字符串
     */
    public String getPutPolicy() {

        String res = new String(UrlSafeBase64.decode(this.encodedPutPolicy));
        JSONObject jsonObject = JSON.parseObject(res);
        if (jsonObject.containsKey("persistentOps")) {
            //如果是视频转码，处理视频转码中的Saveas方法
            String persistentOps = jsonObject.getString("persistentOps").split(";")[0];

            //包含 saveas
            if (persistentOps.contains("saveas")) {

                //设置视频参数
                jsonObject.put("voptions",fomatOptions(persistentOps.split("\\|")[0]));

                //如果有saveas 的方法,从saveas中获取转码之后的文件名
                String[] saveas = new String(UrlSafeBase64.decode(persistentOps.split("\\|")[1].replace("saveas/", "")))
                        .split(":");
                if (saveas.length == 2) {
                    jsonObject.put("outkey", saveas[1]);
                }
            }else {
                jsonObject.put("outkey", "");
            }
        }

        log.debug("解析 token 数据 【"+jsonObject.toJSONString()+"】");
        return jsonObject.toJSONString();
    }

    /**
     * 检查sign是否下砸
     */
    public boolean isValidCallback(String accessKey) {
        //根据encodedPutPolicy 使用hmac_sha1进行签名
        String sign = "";
        try {
            sign = Hmacsha1.getSignature(this.encodedPutPolicy, this.secretKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String encodedSign = UrlSafeBase64.encodeToString(sign);
        return encodedSign.equals(this.encodedSign) && accessKey.equals(this.accessKey);
    }


    /**
     * 格式化视频参数
     * @param vOptions
     * @return
     */
    private JSONObject fomatOptions(String vOptions){
        String[] voptArray = vOptions.split("/");
        JSONObject optJson = new JSONObject();
        for(int i=0; i<voptArray.length;i++){
            int k = i;
            int j=++i;
            if(j>=voptArray.length){
                break;
            }
            optJson.put(voptArray[k],voptArray[j]);
        }

        return  optJson;
    }

}
