package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 配送单详情
 */
@Entity
@Data
public class DispatchDetail {

    @Id
    private String id;

    /** 配送主表id. */
    private String dispatchId;

    /** 商品id. */
    private String productId;

    /** 商品名称. */
    private String productName;

    /** 小图. */
    private String productIcon;

    /** 商品价格. */
    private BigDecimal productPrice;

    /** 商品数量. */
    private Integer productQuantity;


}
