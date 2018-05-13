package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dataobject.Theme;
import com.hnust.wxsell.dto.ProductDTO;

import java.util.List;

/**
 * 主题
 * Create by HJT
 * 2018/2/2 13:29
 **/
public interface ThemeService {
 
    /**查询一个主题*/
    Theme findOne(Integer themeId);

    /**查找所有主题*/
    List<Theme> findAll();

    /**查询主题下所有商品*/
    List<ProductDTO> findByThemeId(Integer themeId,String schoolNo);
}
