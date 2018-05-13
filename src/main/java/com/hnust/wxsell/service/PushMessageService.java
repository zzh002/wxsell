package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.OrderDTO;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:28
 **/
public interface PushMessageService {

    /**
     * 订单状态变更消息
     * @param orderDTO
     */
    void orderStatus(OrderDTO orderDTO);

    void paid(OrderDTO orderDTO,String openid);
}
