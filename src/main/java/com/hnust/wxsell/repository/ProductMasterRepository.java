package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 13:39
 **/
public interface ProductMasterRepository extends JpaRepository<ProductMaster , String> {

    List<ProductMaster> findByCategoryType(Integer categoryType);

    List<ProductMaster> findByProductIdIn(List<String> productIdList);
}
