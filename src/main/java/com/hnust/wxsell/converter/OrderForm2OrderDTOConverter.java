package com.hnust.wxsell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:31
 **/
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
