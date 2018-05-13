package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:42
 **/
public interface PayService {
    PayResponse create(OrderDTO orderDTO);

    PayResponse notify(String notifyData);

    RefundResponse refund(OrderDTO orderDTO);

    boolean query(String orderId);

    void  queryOrderList(List<OrderDTO> orderDTOList);
}
