package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.DispatchMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

/**
 * @author ZZH
 * @date 2018/4/9 0009 12:34
 **/
public interface DispatchMasterRepository extends JpaRepository<DispatchMaster, String> {
    DispatchMaster findBySchoolNoAndGroupNo(String schoolNo , String groupNo);

    Page<DispatchMaster> findBySchoolNoAndDispatchAmountGreaterThan(String schoolNo ,BigDecimal amount, Pageable pageable);
}
