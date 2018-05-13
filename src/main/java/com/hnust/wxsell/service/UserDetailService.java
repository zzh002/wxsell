package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.UserDetail;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 12:47
 **/
public interface UserDetailService {

    UserDetail findOne(String id);

    UserDetail findByOpenId(String openId);

    UserDetail save(UserDetail userDetail);

}
