package com.hnust.wxsell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.DispatchDetail;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 17:01
 **/
@Data
public class DispatchDTO {
    private String dispatchId;

    private String schoolNo;

    private String groupNo;

    private String groupDistrict;

    /**配送的商品种类 */
    private Integer dispatchKind;

    /** 配送总金额 */
    private BigDecimal dispatchAmount;

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<DispatchDetail> dispatchDetailList;
}
