package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:17
 **/
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrderId(String orderId);

    List<OrderDetail> findByOrderIdIn(List<String> orderIdList);
}
