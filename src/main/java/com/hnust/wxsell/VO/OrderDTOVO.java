package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/8 0008 16:42
 **/
@Data
public class OrderDTOVO implements Serializable {

    private static final long serialVersionUID = 1719327242137608149L;



    private String orderId;


    /** 寝室地址. */
    private String groupNo;


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

    List<OrderDetailVO> orderDetailVOList;

}
