package com.hnust.wxsell.dataobject;

import com.hnust.wxsell.enums.OrderStatusEnum;
import com.hnust.wxsell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class OrderMaster {
    /** 订单id. */
    @Id
    private String orderId;

    /** 学校编号*/
    private String schoolNo;

    /** 寝室地址. */
    private String groupNo;

    /** 买家微信Openid. */
    private String userOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
