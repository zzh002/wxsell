package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.School;
import com.hnust.wxsell.repository.SchoolRepositroy;
import com.hnust.wxsell.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 22:30
 **/
@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolRepositroy repositroy;

    @Override
    public School findOne(String schoolId) {
        return repositroy.findOne(schoolId);
    }

    @Override
    public School findBySchoolNo(String schoolNo) {
        return repositroy.findBySchoolNo(schoolNo);
    }

    @Override
    public List<School> findAll() {
        return repositroy.findAll();
    }

    @Override
    public List<School> findBySchoolNoIn(List<String> schoolNoList) {
        return repositroy.findBySchoolNoIn(schoolNoList);
    }

    @Override
    public School save(School school) {
        return repositroy.save(school);
    }
}
