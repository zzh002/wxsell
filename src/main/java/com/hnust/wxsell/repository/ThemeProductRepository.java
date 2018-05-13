package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ThemeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by HJT
 * 2018/3/19 11:14
 **/
public interface ThemeProductRepository extends JpaRepository<ThemeProduct,String> {

    List<ThemeProduct> findByThemeId(Integer themeId);
}
