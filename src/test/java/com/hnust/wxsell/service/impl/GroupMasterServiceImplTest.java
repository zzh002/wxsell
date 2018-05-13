package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.utils.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/7 0007 19:27
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GroupMasterServiceImplTest {
    @Autowired
    private GroupMasterService groupMasterService;

    @Test
    public void findBySchoolNo() {
    }

    @Test
    public void findByGroupDistrictAndSchoolNo() {
        PageRequest request = new PageRequest(0, 10);
        Page<GroupMaster> groupMasterPage = groupMasterService.
                findByGroupDistrictAndSchoolNo("7","1", request);
       log.info("groupMasterList={}",groupMasterPage);
        Assert.assertNotEquals(0, groupMasterPage.getTotalPages());
    }

    @Test
    public void findOne() {
    }

    @Test
    public void save() {
    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,5);
        Page<GroupMasterDTO> groupMasterDTOPage =  groupMasterService.findList("1",request);
        log.info("groupMasterDTO={}",groupMasterDTOPage.getTotalElements());
        Assert.assertTrue("查询所有的订单列表", groupMasterDTOPage.getTotalElements() > 0);

    }

    @Test
    public void findByGroupNoAndSchoolNo() {
    }

    @Test
    public void consumePaid() {
    }

    @Test
    public void consumeRefund() {
    }
}