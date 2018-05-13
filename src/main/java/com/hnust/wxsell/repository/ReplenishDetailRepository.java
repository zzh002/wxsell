package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ReplenishDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:46
 **/
public interface ReplenishDetailRepository extends JpaRepository<ReplenishDetail,String> {
    List<ReplenishDetail> findByReplenishId(String replenishId);
}
