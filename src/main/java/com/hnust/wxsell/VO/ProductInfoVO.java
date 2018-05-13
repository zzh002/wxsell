package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详情
 **/
@Data
public class ProductInfoVO implements Serializable {

    private static final long serialVersionUID = -2124067132333811718L;

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    private BigDecimal purchasePrice;

    @JsonProperty("status")
    private Integer productStatus;

    @JsonProperty("stock")
    private Integer productStock;

    @JsonProperty("stockout")
    private Integer productStockout;

    @JsonProperty("sales")
    private Integer productSales;

    @JsonProperty("quantity")
    private Integer productQuantity;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;
}

