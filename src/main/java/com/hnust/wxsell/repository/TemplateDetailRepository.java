package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.TemplateDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by HJT
 * 2018/3/19 11:14
 **/
public interface TemplateDetailRepository extends JpaRepository<TemplateDetail, String> {

    List<TemplateDetail> findByTemplateId(String templateId);
}
