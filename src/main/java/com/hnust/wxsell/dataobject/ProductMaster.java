package com.hnust.wxsell.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ProductMaster {

    @Id
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 进价. */
    private BigDecimal purchasePrice;


    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;


    /** 类目编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
