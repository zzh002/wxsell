package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.GroupDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 15:56
 **/
public interface GroupDistrictRepository extends JpaRepository<GroupDistrict,Integer> {
    List<GroupDistrict> findBySchoolNoAndGroupDistrictIn( String schoolNo,List<String> groupDistrictList);

    GroupDistrict findByGroupDistrictAndSchoolNo(String groupDistrict , String schoolNo);

    List<GroupDistrict> findBySchoolNo(String schoolNo);
}
