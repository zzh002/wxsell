package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.ReplenishDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/17 0017 12:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ReplenishServiceImplTest {
    @Autowired
    private ReplenishServiceImpl replenishService;

    @Test
    public void create() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findList() {
    }

    @Test
    public void cancel() {
    }

    @Test
    public void finish() {
    }

    @Test
    public void findListBySchoolNo() {
    }

    @Test
    public void findNewReplenish() {
    }

    @Test
    public void save() {
    }

    @Test
    public void delete() {
        List<ReplenishDetail> replenishDetailList = replenishService.findOne("1523704558882322531")
                .getReplenishDetailList();
        ReplenishDetail replenishDetail = replenishDetailList.get(2);
         replenishService.delete(replenishDetail);
         log.info("replenishDetail={}",replenishDetail);
    }
}