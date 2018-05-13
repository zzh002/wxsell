package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ProductInfoVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.VO.ThemeVO;
import com.hnust.wxsell.dataobject.Theme;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.service.ThemeService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/14 0014 16:48
 **/
@RestController
@RequestMapping("/buyer/theme")
@Slf4j
public class BuyerThemeController {
    @Autowired
    private ThemeService themeService;
    @Autowired
    private UserTokenService userTokenService;

    @GetMapping("/list")
    public ResultVO list(){
        List<Theme> themeList = themeService.findAll();

        List<ThemeVO> themeVOList = new ArrayList<>();
        for (Theme theme: themeList){
            ThemeVO themeVO = new ThemeVO();
            BeanUtils.copyProperties(theme,themeVO);
            themeVOList.add(themeVO);
        }

        return ResultVOUtil.success(themeVOList);
    }

    @GetMapping("/products")
    public ResultVO products(@RequestParam("themeId") Integer themeId,
                             @RequestParam("token") String token){

        UserMaster userMaster = userTokenService.getUserMaster(token);
        Theme theme = themeService.findOne(themeId);
        List<ProductDTO> productDTOList = themeService.findByThemeId(themeId,userMaster.getSchoolNo());

        ThemeVO themeVO = new ThemeVO();
        BeanUtils.copyProperties(theme,themeVO);

        List<ProductInfoVO> productInfoVOList = new ArrayList<>();
        for(ProductDTO productDTO: productDTOList){
            ProductInfoVO productInfoVO = new ProductInfoVO();
            BeanUtils.copyProperties(productDTO,productInfoVO);
            productInfoVOList.add(productInfoVO);
        }

        themeVO.setProductInfoVOList(productInfoVOList);

        return ResultVOUtil.success(themeVO);
    }
}
