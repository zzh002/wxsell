package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 20:54
 **/
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String>,
        JpaSpecificationExecutor<OrderMaster> {
        Page<OrderMaster> findByUserOpenid(String openId, Pageable pageable);

        List<OrderMaster> findByPayStatus(Integer payStatus);

        Page<OrderMaster> findBySchoolNo(String schoolNo ,Pageable pageable);
}
