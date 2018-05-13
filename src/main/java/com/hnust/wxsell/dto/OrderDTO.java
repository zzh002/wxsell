package com.hnust.wxsell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.enums.OrderStatusEnum;
import com.hnust.wxsell.enums.PayStatusEnum;
import com.hnust.wxsell.utils.EnumUtil;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 0:05
 **/
@Data
public class OrderDTO {
    private String orderId;

    /** 学校编号*/
    private String schoolNo;

    /** 寝室地址. */
    private String groupNo;

    /** 买家微信Openid. */
    @JsonIgnore
    private String userOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus ;

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus ;

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }
}
