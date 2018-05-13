package com.hnust.wxsell.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 **/
@Component
public class WechatMaConfig {

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaConfig());
        return wxMaService;
    }

    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaInMemoryConfig wxMaInMemoryConfig = new WxMaInMemoryConfig();
        wxMaInMemoryConfig.setAppid(accountConfig.getMpAppId());
        wxMaInMemoryConfig.setSecret(accountConfig.getMpAppSecret());
        return wxMaInMemoryConfig;
    }
}
