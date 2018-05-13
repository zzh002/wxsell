package com.hnust.wxsell.form;

import lombok.Data;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:16
 **/
@Data
public class OrderListForm {

    private String token;
    /**订单状态 */
    private Integer orderStatus;

    /** 支付状态 */
    private Integer payStatus;

    /** 寝室地址，模糊查找*/
    private String groupNo;

    /** 学校编号*/
    private String schoolNo;

    /** 微信openid */
    private String openid;

    /**查找的起始时间 */
    private String startTime;
    private String endTime;
}
