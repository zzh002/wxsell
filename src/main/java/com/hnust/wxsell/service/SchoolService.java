package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.School;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 22:26
 **/
public interface SchoolService {

    School findOne(String schoolId);

    School findBySchoolNo(String schoolNo);

    List<School> findAll();

    List<School> findBySchoolNoIn(List<String> schoolNoList);

    School save(School school);
}
