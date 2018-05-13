package com.hnust.wxsell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@DynamicUpdate
public class ReplenishDetail {

    @Id
    private String detailId;

    /** 订单id. */
    private String replenishId;

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

    public ReplenishDetail(){
    }

    public ReplenishDetail(String productId, Integer productQuantity){
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
