package com.hnust.wxsell.controller;

import com.hnust.wxsell.config.ProjectUrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZZH
 * @date 2018/4/11 0011 20:44
 **/
@Controller
@RequestMapping("/seller")
public class SellerMenuController {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/mainMenu")
    public String mianMenu(){
        //TODO
        String url = projectUrlConfig.getSell();
        return "redirect:"+url;
    }
}
