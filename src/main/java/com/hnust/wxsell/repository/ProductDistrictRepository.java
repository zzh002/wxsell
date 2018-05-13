package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ProductDistrict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 15:38
 **/
public interface ProductDistrictRepository extends JpaRepository<ProductDistrict , String> {

    List<ProductDistrict> findBySchoolNoAndProductStatus(String schoolNo , Integer productStatus);

    List<ProductDistrict> findByProductId(String productId);

    ProductDistrict findByProductIdAndSchoolNo(String productId ,String schoolNo);

    Page<ProductDistrict> findBySchoolNo(String schoolNo ,Pageable pageable);

    List<ProductDistrict> findBySchoolNo(String schoolNo);
}
