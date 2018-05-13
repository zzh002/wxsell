package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.service.UserMasterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/7 0007 11:15
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserMasterServiceImplTest {

    @Autowired
    private UserMasterService userMasterService;

    @Test
    public void save() {
        UserMaster userMaster = new UserMaster();
        userMaster.setSchoolNo("1");
        userMaster.setGroupNo("7-6-406");
        userMaster.setOpenId("123");

        UserMaster result = userMasterService.save(userMaster);

        Assert.assertNotNull(result);
    }


    @Test
    public void checkUserInfo() {
        userMasterService.checkUserInfo("1","7-6-406","123");
    }

    @Test
    public void findByOpenid() {
    }

    @Test
    public void findBySchoolNoAndGroupNo() {
    }
}