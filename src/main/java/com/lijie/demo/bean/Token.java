package com.lijie.demo.bean;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.security.GeneralSecurityException;

/**
 * Created by jieli on 2017/11/2.
 */
public class Token {

    private String accessKey;
    private String secretKey;
    private String encodedSign;
    private String encodedPutPolicy;

    public Token(String token, String secretkey){
        this.secretKey = secretkey;
        String[] result = token.split(":");
        this.accessKey = result[0];
        this.encodedSign = result[1];
        this.encodedPutPolicy = result[2];
    }

    /**
     * 得到token里面保存的json字符串
     */
    public String getPutPolicy(){
        String res = new String(UrlSafeBase64.decode(this.encodedPutPolicy));
        return res;
    }
    
    /**
     * 检查sign是否下砸 
     */
    public boolean isValidCallback(String accessKey) {
        //根据encodedPutPolicy 使用hmac_sha1进行签名
        String sign="";
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
