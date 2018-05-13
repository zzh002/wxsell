package com.hnust.wxsell.config;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Create by HJT
 * 2018/3/19 11:07
 **/
public class MyConfig implements WXPayConfig {

    private byte[] certData;

    public MyConfig() throws Exception {
        String certPath = "D:/HJT/WXtestxxxxxxxxxxxx";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wx735cxxxxxxbb2d";
    }

    public String getMchID() {
        return "148xxxx62";
    }

    public String getKey() {
        return "qwertyuixxxxxxxxxxxxxxxx3456";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}