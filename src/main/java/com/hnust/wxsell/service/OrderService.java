package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.form.OrderListForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 21:00
 **/
public interface OrderService {
    /** 创建订单. */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单. */
    OrderDTO findOne(String orderId);

    /** 查询订单列表，买家端. */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /** 取消订单. */
    OrderDTO cancel(OrderDTO orderDTO);

    /** 完结订单. */
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单. */
    OrderDTO paid(OrderDTO orderDTO);

    /** 查询订单列表,卖家端. */
    Page<OrderDTO> findListBySchoolNo(String schoolNo ,Pageable pageable);

    /** 多条件查询(用户地址/订单状态/支付状态) */
    Page<OrderDTO> complexFindList(OrderListForm orderListForm, Pageable pageable);

    /** 通过日期起始时间查询订单列表 */
    List<OrderDTO> findByTimeBetween(OrderListForm orderListForm);

    /** 查询一段时间内商品销售量 */
    List<OrderDetail> productSalesVolume(OrderListForm orderListForm);

    /** 查询寝室订单列表，卖家端. */
//    Page<OrderDTO> findGroupList(String groupId, Pageable pageable);
}
