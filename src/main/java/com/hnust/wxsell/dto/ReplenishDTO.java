package com.hnust.wxsell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 17:01
 **/
@Data
public class ReplenishDTO {
    private String replenishId;

    /** 名字. */
    private String userName;

    /** 手机号. */
    private String userPhone;

    /** 地址. */
    private String groupNo;

    /** 学校编号*/
    private String schoolNo;

    /** openId. */
    private String openId;

    /** 订单总金额. */
    private BigDecimal replenishAmount;

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 订单状态, 默认为0新下单. */
    private Integer replenishStatus;

    List<ReplenishDetail> replenishDetailList;
}
