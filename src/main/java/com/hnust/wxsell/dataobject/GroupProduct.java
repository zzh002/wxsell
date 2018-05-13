package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class GroupProduct {

    @Id
    private String id;

    private String schoolNo;

    /** 寝室地址编号. */
    private String groupNo;

    /** 商品id. */
    private String productId;

    /** 商品名称. */
    private String productName;

    /** 商品单价. */
    private BigDecimal productPrice;

    /** 商品库存. */
    private Integer productStock;

    /** 商品回收库存. */
    private Integer productStockout = 0;

    /** 商品销量. */
    private Integer productSales = 0;

    /** 商品总数量. */
    private Integer productQuantity;

    /** 描述. */
    private String productDescription;

    /** 商品小图. */
    private String productIcon;

    /** 类目编号. */
    private Integer categoryType;
}
