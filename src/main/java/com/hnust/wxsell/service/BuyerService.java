package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.OrderDTO;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:22
 **/
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
