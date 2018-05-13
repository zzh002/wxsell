package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dto.BannerDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.BannerService;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZZH
 * @date 2018/4/3 0003 22:15
 **/
@RestController
@RequestMapping("/buyer/banner")
@Slf4j
public class BuyerBannerController {
    @Autowired
    BannerService bannerService;

    @GetMapping("/list")
    public ResultVO list(@RequestParam("bannerId") Integer bannerId){
        if (StringUtils.isEmpty(bannerId)) {
            log.error("【查询轮播图】bannerId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        BannerDTO bannerDTO = bannerService.findByBannerId(bannerId);

        return ResultVOUtil.success(bannerDTO);
    }
}
