package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dto.DispatchDTO;
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
 * @date 2018/4/17 0017 12:34
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DispatchServiceImplTest {

    @Autowired
    private DispatchServiceImpl dispatchService;

    @Test
    public void findOne() {
        DispatchDTO dispatchDTO = dispatchService.findOne("1","2-1-102");
        Assert.assertNotNull(dispatchDTO);
    }

    @Test
    public void add() {
    }

    @Test
    public void cancel() {
    }

    @Test
    public void finish() {
    }

    @Test
    public void findList() {
    }
}