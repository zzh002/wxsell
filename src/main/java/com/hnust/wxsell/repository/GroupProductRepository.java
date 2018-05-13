package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.GroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 16:17
 **/
public interface GroupProductRepository extends JpaRepository<GroupProduct, String> {

    List<GroupProduct> findBySchoolNoAndGroupNo(String schoolNo , String groupNo);

    GroupProduct findBySchoolNoAndGroupNoAndProductId(String schoolNo ,
                                                      String groupNo, String productId);

  //  void removeAllById(String Id);

}
