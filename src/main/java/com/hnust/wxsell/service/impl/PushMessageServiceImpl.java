package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.config.WechatAccountConfig;
import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.PayStatusEnum;
import com.hnust.wxsell.form.OrderListForm;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:29
 **/
@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig accountConfig;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GroupMasterService groupMasterService;
    @Override
    public void orderStatus(OrderDTO orderDTO) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateId().get("orderStatus"));
        templateMessage.setToUser(orderDTO.getUserOpenid());
        templateMessage.setUrl(projectUrlConfig.getSell() + "/sell/pay/view?orderId=" + orderDTO.getOrderId());

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first", "纯享吃呗订单","#589E63"),
                new WxMpTemplateData("OrderSn", orderDTO.getOrderId()),
                new WxMpTemplateData("OrderStatus", orderDTO.getPayStatusEnum().getMessage()),
                new WxMpTemplateData("remark", "点击“详情”查看完整订单信息")
        );
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (WxErrorException e) {
            log.error("【微信模版消息】发送失败, {}", e);
        }
    }

    @Override
    public void paid(OrderDTO orderDTO, String openid) {

        //拼装订单商品详情信息
        String productDetail = " \n";
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()){
            productDetail = productDetail.concat(orderDetail.getProductName() + " 数量: "+ orderDetail.getProductQuantity().toString() + "\n");
        }

        //查询当日订单数量、销售金额
        BigDecimal orderListAmount = new BigDecimal(BigInteger.ZERO);
        OrderListForm orderListForm = new OrderListForm();
        orderListForm.setStartTime(DateUtil.getTodayTime());
        orderListForm.setEndTime(DateUtil.getTomorrowTime());
        orderListForm.setPayStatus(PayStatusEnum.SUCCESS.getCode());

        List<OrderDTO> orderDTOList = orderService.findByTimeBetween(orderListForm);

        /**计算所有订单总价 */
        for (OrderDTO order: orderDTOList){
            orderListAmount = order.getOrderAmount().add(orderListAmount);
        }

        //查找寝室
        GroupMasterDTO groupMasterDTO = groupMasterService.
                findByGroupNoAndSchoolNo(orderDTO.getGroupNo(),orderDTO.getSchoolNo());

        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateId().get("paid"));
        templateMessage.setToUser(openid);
        templateMessage.setUrl(projectUrlConfig.getSell() + "/sell/pay/view?orderId=" + orderDTO.getOrderId());

        List<WxMpTemplateData> data = Arrays.asList(
                new WxMpTemplateData("first",
                        "今日第"+ String.valueOf(orderDTOList.size())+ "单，"+ orderDTO.getGroupNo() + "支付成功" +
                                "\n"+ "累计收入: ￥" + orderListAmount.toString() ,"#589E63"),
                new WxMpTemplateData("orderMoneySum", "￥"+orderDTO.getOrderAmount().toString(),"#589E63"),
                new WxMpTemplateData("orderProductName", productDetail,"#589E63"),
                new WxMpTemplateData("Remark", orderDTO.getGroupNo()+" 剩余商品总金额: ￥" + groupMasterDTO.getGroupAmount().toString(),"#589E63")
        );
        templateMessage.setData(data);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (WxErrorException e) {
            log.error("【微信模版消息】发送失败, {}", e);
        }
    }

}
