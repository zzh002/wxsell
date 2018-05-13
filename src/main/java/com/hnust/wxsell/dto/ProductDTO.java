package com.hnust.wxsell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.ProductDistrict;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 15:55
 **/
@Data
public class ProductDTO {

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

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<ProductDistrict> productDistrictList;
}
