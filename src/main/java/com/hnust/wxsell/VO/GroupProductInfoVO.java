package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 寝室商品详情
 **/
@Data
public class GroupProductInfoVO implements Serializable {

    private static final long serialVersionUID = 4599103299496349917L;

    @JsonProperty("id")
    private String productId;


    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

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
