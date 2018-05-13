package com.hnust.wxsell.dataobject;


import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class GroupMaster {

    @Id
    private String groupId;

    /** 宿舍编号. */
    private String groupNo;

    /** 宿舍分区. */
    private String groupDistrict;

    /** 学校编号 */
    private String schoolNo;

    /** 宿舍商品总金额. */
    private BigDecimal groupAmount = new BigDecimal(BigInteger.ZERO);

    /** 宿舍消费总金额. */
    private BigDecimal groupConsume = new BigDecimal(BigInteger.ZERO);

    /** 申请人姓名. */
    private String userName;

    /** 申请人电话. */
    private String userPhone;

    /** 补货信息快照 */
    private String snapItems;

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
