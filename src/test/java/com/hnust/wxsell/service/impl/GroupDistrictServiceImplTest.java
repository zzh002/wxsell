package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.GroupDistrict;
import com.hnust.wxsell.service.GroupDistrictService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/7 0007 16:09
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GroupDistrictServiceImplTest {

    @Autowired
    private GroupDistrictService groupDistrictService;

    @Test
    public void findOne() {
        GroupDistrict result = groupDistrictService.findOne(123456);
        Assert.assertNotNull(result);
    }

    @Test
    public void findAll() {
        List<GroupDistrict> groupDistrictList = groupDistrictService.findAll("1");
        log.info("【查找结果】 result={}", groupDistrictList );
        Assert.assertNotNull(groupDistrictList);
    }

    @Test
    public void findBySchoolNoAndGroupDistrictIn() {
        List<GroupDistrict> groupDistrictList = groupDistrictService.
                findBySchoolNoAndGroupDistrictIn("1", Arrays.asList("1", "2", "3"));
        log.info("【查找结果】 result={}", groupDistrictList );

        Assert.assertNotNull(groupDistrictList);
    }

    @Test
    public void save() {
    }
}