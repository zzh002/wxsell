package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.TemplateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by HJT
 * 2018/3/19 11:14
 **/
public interface TemplateMasterRepository extends JpaRepository<TemplateMaster, String> {

    List<TemplateMaster> findByDeleteStatusAndSchoolNo(Integer deleteStatus , String schoolNo);
    TemplateMaster findBySchoolNoAndTemplateName(String schoolNo , String groupNo);
}
