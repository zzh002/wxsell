package com.hnust.wxsell.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZZH
 * @date 2018/4/12 0012 22:21
 **/
@Data
public class ProductForm {



    private String productId;

    /** 名字. */
    private String productName;

    /** 售价. */
    private BigDecimal productPrice;

    /** 进价*/
    private BigDecimal purchasePrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 类目编号. */
    private Integer categoryType;
}
