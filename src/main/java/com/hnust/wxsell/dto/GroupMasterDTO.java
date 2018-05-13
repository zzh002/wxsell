package com.hnust.wxsell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 23:59
 **/
@Data
public class GroupMasterDTO {

    private String gourpId;

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
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    /** 补货详情单. */
    List<GroupProduct> groupProductList;
}
