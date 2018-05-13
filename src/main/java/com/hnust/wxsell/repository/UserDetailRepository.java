package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 12:27
 **/
public interface UserDetailRepository extends JpaRepository<UserDetail,String> {
    UserDetail findByOpenId(String openId);



}
