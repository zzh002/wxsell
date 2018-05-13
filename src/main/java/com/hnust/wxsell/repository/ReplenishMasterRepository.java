package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.ReplenishMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:40
 **/
public interface ReplenishMasterRepository extends JpaRepository<ReplenishMaster,String> {

    Page<ReplenishMaster> findByOpenId(String openId , Pageable pageable);

    Page<ReplenishMaster> findByReplenishStatusAndSchoolNo(Integer replenishStatus,String schoolNo, Pageable pageable);

    Page<ReplenishMaster> findBySchoolNo(String schoolNo ,Pageable pageable );
}
