package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.UserMaster;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 11:34
 **/
public interface UserMasterService {

    /**用户注册*/
    UserMaster save(UserMaster userMaster);

    //确认用户信息
    void checkUserInfo(String schoolNo ,String groupNo, String openId);

    /** 通过openid查找用户 */
    UserMaster findByOpenid(String groupOpenid);

    /** 通过学校编号和寝室编号查用户*/
    List<UserMaster> findBySchoolNoAndGroupNo(String schoolNo, String groupNo);

    /**通过Id查找用户信息*/
    UserMaster findOne(String userId);
}
