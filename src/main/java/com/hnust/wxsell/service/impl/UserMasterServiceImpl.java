package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.GroupMasterRepository;
import com.hnust.wxsell.repository.UserMasterRepository;
import com.hnust.wxsell.service.UserMasterService;
import com.hnust.wxsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 11:37
 **/
@Service
@Slf4j
public class UserMasterServiceImpl implements UserMasterService{
    @Autowired
    private UserMasterRepository userMasterRepository;
    @Autowired
    private GroupMasterRepository groupMasterRepository;

    @Override
    public UserMaster save(UserMaster userMaster) {
        //查询寝室
        GroupMaster groupMaster = groupMasterRepository.
                findByGroupNoAndSchoolNo(userMaster.getGroupNo(),userMaster.getSchoolNo());
        if (groupMaster == null){
            log.error("【寝室未注册】, groupNo={}", userMaster.getGroupNo());
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }
        //查询用户是否已经注册, 注册了则更新
        userMaster.setUserId(KeyUtil.genUniqueKey());
        UserMaster findResult = userMasterRepository.findByOpenId(userMaster.getOpenId());
        if (findResult != null){
            userMaster.setUserId(findResult.getUserId());
        }
        //注册

        return userMasterRepository.save(userMaster);
    }

    @Override
    public void checkUserInfo(String schoolNo, String groupNo, String openId) {

        //查询寝室
        GroupMaster groupMaster = groupMasterRepository.
                findByGroupNoAndSchoolNo(groupNo,schoolNo);
        if (groupMaster == null){
            log.error("【用户信息错误】, groupNo={}, openId={}", groupNo, openId);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        //核对用户信息
        UserMaster userMaster = userMasterRepository.findBySchoolNoAndGroupNoAndOpenId(schoolNo,groupNo, openId);
        if(userMaster == null){
            log.error("【用户信息错误】, groupNo={}, openId={}", groupNo, openId);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
    }

    @Override
    public UserMaster findByOpenid(String groupOpenid) {
        return userMasterRepository.findByOpenId(groupOpenid);
    }

    @Override
    public List<UserMaster> findBySchoolNoAndGroupNo(String schoolNo, String groupNo) {
        return userMasterRepository.findBySchoolNoAndGroupNo(schoolNo,groupNo);
    }

    @Override
    public UserMaster findOne(String userId) {
        return userMasterRepository.findOne(userId);
    }
}
