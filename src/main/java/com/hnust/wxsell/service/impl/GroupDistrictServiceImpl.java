package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.GroupDistrict;
import com.hnust.wxsell.repository.GroupDistrictRepository;
import com.hnust.wxsell.service.GroupDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 16:05
 **/
@Service
public class GroupDistrictServiceImpl implements GroupDistrictService {

    @Autowired
    GroupDistrictRepository groupDistrictRepository;

    @Override
    public GroupDistrict findOne(Integer id) {
        return groupDistrictRepository.findOne(id);
    }

    @Override
    public List<GroupDistrict> findAll(String schoolNo) {
        return groupDistrictRepository.findBySchoolNo(schoolNo);
    }

    @Override
    public List<GroupDistrict> findBySchoolNoAndGroupDistrictIn(String schoolNo, List<String> groupDistrictList) {
        return groupDistrictRepository.findBySchoolNoAndGroupDistrictIn(schoolNo,groupDistrictList);
    }

    @Override
    public GroupDistrict save(GroupDistrict groupDistrict) {
        return groupDistrictRepository.save(groupDistrict);
    }
}
