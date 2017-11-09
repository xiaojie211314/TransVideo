package com.qiniu.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by jieli on 2017/11/2.
 */
public class Token {

    private String accessKey;
    private String secretKey;
    private String encodedSign;
    private String encodedPutPolicy;

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
    public JSONObject getPutPolicy() {

        String res = new String(UrlSafeBase64.decode(this.encodedPutPolicy));
        JSONObject jsonObject = JSONObject.parseObject(res);
        if (jsonObject.containsKey("persistentOps")) {
            //如果是视频转码，处理视频转码中的Saveas方法
            String[] persitentString = jsonObject.getString("persistentOps").split("|");
            if (persitentString.length == 2) {
                //如果有saveas 的方法,从saveas中获取转码之后的文件名
                String[] saveas = new String(UrlSafeBase64.decode(persitentString[1].replace("saveas/", "")))
                        .split(":");
                if (saveas.length == 2) {
                    jsonObject.put("outkey", saveas[1]);
                }
            }
            jsonObject.put("outkey", "");
        }
        return jsonObject;
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

}
