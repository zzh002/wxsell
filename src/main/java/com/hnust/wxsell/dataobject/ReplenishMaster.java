package com.hnust.wxsell.dataobject;


import com.hnust.wxsell.enums.OrderStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class ReplenishMaster {
    /** 订单id. */
    @Id
    private String replenishId;

    /** 名字. */
    private String userName;

    /** 手机号. */
    private String userPhone;

    /** 地址. */
    private String groupNo;

    /** 学校编号*/
    private String schoolNo;

    /** userId外键. */
    private String openId;

    /** 订单总金额. */
    private BigDecimal replenishAmount;

    /** 创建时间. */
    private Date createTime;

    /* 修改时间*/
    private Date updateTime;

    /** 订单状态, 默认为0新下单. */
    private Integer replenishStatus = OrderStatusEnum.NEW.getCode();
}
