package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.UserDetail;
import com.hnust.wxsell.repository.UserDetailRepository;
import com.hnust.wxsell.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 12:53
 **/
@Service
public class UserDetailServiceImpl implements UserDetailService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    public UserDetail findOne(String id) {
        return userDetailRepository.findOne(id);
    }

    @Override
    public UserDetail findByOpenId(String openId) {
        return userDetailRepository.findByOpenId(openId);
    }

    @Override
    public UserDetail save(UserDetail userDetail) {
        return userDetailRepository.save(userDetail);
    }
}
