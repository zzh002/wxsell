package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProductCategory 实体类型
 * Integer 主键类型
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory findByCategoryType(Integer categoryType);
}
