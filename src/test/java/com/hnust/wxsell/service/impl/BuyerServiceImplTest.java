package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dto.OrderDTO;
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
 * @date 2018/4/17 0017 12:18
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BuyerServiceImplTest {

    @Autowired
    private BuyerServiceImpl buyerService;

    @Test
    public void findOrderOne() {
        OrderDTO orderDTO = buyerService.findOrderOne("obyKk0xQu8ar5I6L1jy5CSK8VjCw","1523628824038522078");
        Assert.assertNotNull(orderDTO);
    }

    @Test
    public void cancelOrder() {
        OrderDTO orderDTO = buyerService.cancelOrder("obyKk0xQu8ar5I6L1jy5CSK8VjCw","1523628824038522078");
        Assert.assertNotNull(orderDTO);
    }
}