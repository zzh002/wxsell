package com.hnust.wxsell.dto;

import lombok.Data;

/**
 * @author ZZH
 * @date 2018/4/6 0006 16:08
 **/
@Data
public class CartDTO {

    /** 商品Id. */
    private String productId;

    /** 数量. */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
