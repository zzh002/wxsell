package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/10 0010 19:33
 **/
public interface SellerInfoRepository extends JpaRepository<SellerInfo , String> {
    SellerInfo findByOpenid(String openid);

    List<SellerInfo> findBySchoolNo(String schoolNo);
}
