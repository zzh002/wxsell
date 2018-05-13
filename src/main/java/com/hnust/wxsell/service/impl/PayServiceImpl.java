package com.hnust.wxsell.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.hnust.wxsell.component.RedisLock;
import com.hnust.wxsell.config.MyConfig;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.PayStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.OrderService;
import com.hnust.wxsell.service.PayService;
import com.hnust.wxsell.utils.JsonUtil;
import com.hnust.wxsell.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.model.wxpay.response.WxPayAsyncResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.MoneyUtil;
import com.lly835.bestpay.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:43
 **/
@Service
@Slf4j
public class PayServiceImpl implements PayService {
    private static final String ORDER_NAME = "吃呗订单";

    private static final int TIMEOUT = 10 * 1000;    //超时时间十秒

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisLock redisLock;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getUserOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支付, request={}", JsonUtil.toJson(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付, response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //1. 验证签名
        //2. 支付的状态
        //3. 支付金额
        //4. 支付人(下单人 == 支付人)

        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        log.info("【微信支付】异步通知, payResponse={}", JsonUtil.toJson(payResponse));

        OrderDTO orderDTO = changePayStatus(payResponse);

        return payResponse;
    }

    /**
     * 退款
     * @param orderDTO
     * @return
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】request={}", JsonUtil.toJson(refundRequest));

        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】response={}", JsonUtil.toJson(refundResponse));

        return refundResponse;
    }

    /**
     * 查询订单
     * @param notifyData
     * @return
     */
    @Override
    public boolean query(String notifyData) {
        //xml解析为对象
        WxPayAsyncResponse asyncResponse = (WxPayAsyncResponse) XmlUtil.fromXML(notifyData, WxPayAsyncResponse.class);
        PayResponse payResponse = buildPayResponse(asyncResponse);

        //加锁
/*        long time = System.currentTimeMillis() + TIMEOUT;
        if(!redisLock.lock(payResponse.getOrderId(),String.valueOf(time))){
            throw new SellException(ResultEnum.DECREASE_STOCK_ERROR);
        }*/

        Map<String, String> resp = new HashMap<String, String>();
        try {
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", payResponse.getOrderId());

            resp = wxpay.orderQuery(data);

            log.info("【微信订单状态查询】查询结果, resp={}", resp.toString());

        } catch (Exception e) {
            log.error("【微信订单状态查询】捕获异常 resp={}, e={}",resp.toString(), e);

            throw new SellException(ResultEnum.WECHAT_ORDER_QUERY_ERROR);
        }

        if (resp.get("trade_state").equals("SUCCESS")){
            changePayStatus(payResponse);

            //解锁
//            redisLock.unlock(payResponse.getOrderId(), String.valueOf(time));

            return true;
        }

        //解锁
//        redisLock.unlock(payResponse.getOrderId(), String.valueOf(time));

        return false;
    }


    /**
     * 批量修改订单状态，去微信查询
     * @param orderDTOList
     */
    @Override
    @Transactional
    public void queryOrderList(List<OrderDTO> orderDTOList) {

        int count = 0;

        for (OrderDTO orderDTO : orderDTOList){

            Map<String, String> resp = new HashMap<String, String>();
            try {
                MyConfig config = new MyConfig();
                WXPay wxpay = new WXPay(config);

                Map<String, String> data = new HashMap<String, String>();
                data.put("out_trade_no", orderDTO.getOrderId());

                resp = wxpay.orderQuery(data);

                log.info("【微信订单状态查询】查询结果, resp={}", resp.toString());

            } catch (Exception e) {
                log.error("【微信订单状态查询】捕获异常 resp={}, e={}",resp.toString(), e);

//                throw new SellException(ResultEnum.WECHAT_ORDER_QUERY_ERROR);
            }

            if (resp.get("result_code").equals("SUCCESS") && resp.get("trade_state").equals("SUCCESS")){

                //修改订单的支付状态
                if (orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
                    log.info("【修改订单支付状态】，orderDTO={}", orderDTO);

                    orderDTO = orderService.findOne(orderDTO.getOrderId());

                    orderService.paid(orderDTO);
                    count++;

                    log.info("【修改订单支付状态】 修改成功，orderDTO={}", orderDTO);
                }
            }
    }
        log.info("【修改订单数量】 此次修改订单数量，count={}", count);
    }

    private PayResponse buildPayResponse(WxPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTransactionId());
        return payResponse;
    }

    private OrderDTO changePayStatus(PayResponse payResponse){
        //查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());

        //判断订单是否存在
        if (orderDTO == null) {
            log.error("【微信支付】异步通知, 订单不存在, orderId={}", payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //判断金额是否一致(0.10   0.1)
        if (!MathUtil.equals(payResponse.getOrderAmount(), orderDTO.getOrderAmount().doubleValue())) {
            log.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        // TODO 修改订单的支付状态
//        if (orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
        orderService.paid(orderDTO);
//        }

        log.info("【修改订单支付状态】 修改成功，orderDTO={}", orderDTO);

        return orderDTO;
    }
}
