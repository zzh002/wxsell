package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 22:22
 **/
public interface SchoolRepositroy extends JpaRepository<School ,String> {
    School findBySchoolNo(String schoolNo);

    List<School> findBySchoolNoIn(List<String> schoolNoList);
}
