package com.hnust.wxsell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/buyer")
@Slf4j
public class BuyerController {

    /**
     * 买家中心
     * @param map
     * @return
     */
    @RequestMapping("/center")
    public ModelAndView center(Map<String, Object> map) {
        return new ModelAndView("common/bindSuccess", map);
    }
}
