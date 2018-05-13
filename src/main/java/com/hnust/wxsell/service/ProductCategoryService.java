package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.ProductCategory;

import java.util.List;

/**
 * 类目
 **/
public interface ProductCategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}

