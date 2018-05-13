package com.hnust.wxsell.dataobject;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hnust.wxsell.enums.ProductStatusEnum;
import com.hnust.wxsell.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ProductDistrict {

    @Id
    private String Id;

    private String productId;

    /** 库存. */
    private Integer productStock;

    /** 状态, 0正常1下架. */
    private Integer productStatus = ProductStatusEnum.UP.getCode();

    //学校编号
    private String schoolNo;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
