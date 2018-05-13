package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.BoxApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:52
 **/
public interface BoxApplyRepository extends JpaRepository<BoxApply,String> {

    Page<BoxApply> findBySchoolNoAndDeleteStatus(String schoolNo,Integer deleteStatus,
                                                 Pageable pageable);

    Page<BoxApply> findBySchoolNoAndDeleteStatusAndGroupDistrict(String schoolNo,Integer deleteStatus,
                                           String groupDistrict ,Pageable pageable);

}
