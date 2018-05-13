package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.GroupMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/3 0003 15:21
 **/
public interface GroupMasterRepository extends JpaRepository<GroupMaster , String> {
    Page<GroupMaster> findByGroupDistrictAndSchoolNo(String groupDistrict,String schoolNo,  Pageable pageable);

    GroupMaster findByGroupNoAndSchoolNo(String groupNo , String schoolNo);

    List<GroupMaster> findBySchoolNo(String schoolNo);

    Page<GroupMaster> findBySchoolNo(String schoolNo, Pageable pageable);

    List<GroupMaster> findBySchoolNoAndGroupNoContaining(String schoolNo , String groupNo);
}
