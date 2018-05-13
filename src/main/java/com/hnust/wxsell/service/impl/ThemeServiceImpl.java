package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dataobject.Theme;
import com.hnust.wxsell.dataobject.ThemeProduct;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.repository.ThemeProductRepository;
import com.hnust.wxsell.repository.ThemeRepository;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/14 0014 16:27
 **/
@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    ThemeProductRepository themeProductRepository;

    @Autowired
    ProductService productService;

    @Override
    public Theme findOne(Integer themeId) {
        return themeRepository.findOne(themeId);
    }

    @Override
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Override
    public List<ProductDTO> findByThemeId(Integer themeId,String schoolNo) {
        List<ThemeProduct> themeProductList = themeProductRepository.findByThemeId(themeId);

        List<String> productIdList = themeProductList.stream()
                .map(e -> e.getProductId())
                .collect(Collectors.toList());

        List<ProductDTO> productDTOList =  productService.findByProductStatusAndProductIdIn(schoolNo,productIdList);
        return productDTOList;
    }
}
