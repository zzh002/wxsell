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
        wxMaInMemoryConfig.setAppid("wx288322c27892a46e");
        wxMaInMemoryConfig.setSecret("c03f62364d97ec5f355343a00ca99e0a");
        return wxMaInMemoryConfig;
    }
}
