package com.hnust.wxsell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class DispatchMaster {
    @Id
    private String dispatchId;

    private String schoolNo;

    private String groupNo;

    private String groupDistrict;

    /**配送的商品种类 */
    private Integer dispatchKind;

    /** 配送总金额 */
    private BigDecimal dispatchAmount;

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
