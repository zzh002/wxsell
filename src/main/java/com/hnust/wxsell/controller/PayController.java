package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.OrderDTOVO;
import com.hnust.wxsell.VO.OrderDetailVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.OrderService;
import com.hnust.wxsell.service.PayService;
import com.hnust.wxsell.utils.ResultVOUtil;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZZH
 * @date 2018/4/8 0008 13:33
 **/
@CrossOrigin
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        //1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);

        //2. 发起支付
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        try {
            payService.notify(notifyData);
        }catch (RuntimeException e){

            log.info("【微信异步通知】 捕获到异步通知异常，进入查询订单接口");

            if (payService.query(notifyData)){
                return new ModelAndView("pay/success");
            }
            throw new SellException(ResultEnum.WECHAT_NOTIFY_ERROR);
        }

        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }

    @GetMapping("/view")
    public ModelAndView success(@RequestParam("orderId") String orderId,
                                Map<String, Object> map){

        OrderDTO orderDTO = orderService.findOne(orderId);
        map.put("orderDTO", orderDTO);
        return new ModelAndView("pay/view");
    }
}
