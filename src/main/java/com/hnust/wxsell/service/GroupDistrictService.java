package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.GroupDistrict;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 16:02
 **/
public interface GroupDistrictService {

    GroupDistrict findOne(Integer id);

    List<GroupDistrict> findAll(String schoolNo);

    List<GroupDistrict> findBySchoolNoAndGroupDistrictIn( String schoolNo,List<String> groupDistrictList);

    GroupDistrict save(GroupDistrict groupDistrict);
}
