package com.hnust.wxsell.VO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ZZH
 * @date 2018/4/8 0008 16:49
 **/
@Data
public class OrderDetailVO implements Serializable {

    private static final long serialVersionUID = -3616466621141222595L;


    private String detailId;


    /** 商品id. */
    private String productId;

    /** 商品名称. */
    private String productName;

    /** 商品单价. */
    private BigDecimal productPrice;

    /** 商品数量. */
    private Integer productQuantity;

    /** 商品小图. */
    private String productIcon;
}
