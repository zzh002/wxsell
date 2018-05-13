package com.hnust.wxsell.VO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/12 0012 20:30
 **/
@Data
public class OrderVO  implements Serializable {

    private static final long serialVersionUID = 1719327242137608149L;

    private Integer currentPage;

    /**当前页条目数. */
    private Integer size;
    //订单列表总价
    private BigDecimal orderListAmount;
    //订单列表数量
    private Integer orderListSize;

    List<OrderDTOVO> orderDTOVOList;
}
