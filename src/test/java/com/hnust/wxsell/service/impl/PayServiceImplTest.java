package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.service.OrderService;
import com.hnust.wxsell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;
    @Test
    public void create() throws Exception{
        OrderDTO orderDTO = orderService.findOne("1525763810855915549");
        payService.create(orderDTO);
    }

    @Test
    public void refund() throws Exception{
        OrderDTO orderDTO = orderService.findOne("1525936252982266968");
        payService.refund(orderDTO);
    }

}