package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.contant.OpenidConstant;
import com.hnust.wxsell.converter.OrderMaster2OrderDTOConverter;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dataobject.OrderDetail;
import com.hnust.wxsell.dataobject.OrderMaster;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.OrderStatusEnum;
import com.hnust.wxsell.enums.PayStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.OrderListForm;
import com.hnust.wxsell.repository.OrderDetailRepository;
import com.hnust.wxsell.repository.OrderMasterRepository;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/8 0008 10:46
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private GroupProductService groupProductService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private UserMasterService userMasterService;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessageService pushMessageService;


    @Autowired
    private GroupMasterService groupMasterService;

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

//        List<CartDTO> cartDTOList = new ArrayList<>();
        //用户信息验证
        userMasterService.checkUserInfo(orderDTO.getSchoolNo(),orderDTO.getGroupNo(),orderDTO.getUserOpenid());

        //1. 查询商品（数量, 价格）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            GroupProduct groupProduct =  groupProductService.findBySchoolNoAndGroupNoAndProductId
                    (orderDTO.getSchoolNo(), orderDTO.getGroupNo(), orderDetail.getProductId());
            if (groupProduct == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2. 计算订单总价
            orderAmount = groupProduct.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            //订单详情入库
            orderDetail.setOrderId(orderId);
            Integer productQuantity = orderDetail.getProductQuantity();
            BeanUtils.copyProperties(groupProduct, orderDetail);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());

            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }


        //3. 写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4. 扣库存，放到支付成功后才扣库存
        //扣库存后又加库存，这里由于库存判断放在了减库存中做
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        groupProductService.decreaseStock(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        groupProductService.increaseStock(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());

        //发送websocket消息
//        webSocket.sendMessage(orderDTO.getOrderId());
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByUserOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        groupProductService.increaseStock(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        groupProductService.decreaseSales(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        //如果已支付, 需要退款(同时减少寝室消费金额)
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
            groupMasterService.consumeRefund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //修改寝室消费金额
        try {
            groupMasterService.consumePaid(orderDTO);
        }catch (Exception e){
            log.error("【寝室消费金额】修改失败, {}", e);
        }

        //扣库存加销量
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        groupProductService.decreaseStock(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        groupProductService.increaseSales(cartDTOList,orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        //给卖家推送模板消息
        for (String openid: OpenidConstant.SELLEROPENID){
            pushMessageService.paid(orderDTO, openid);
        }

        // 给用户推送模板消息
//        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findListBySchoolNo(String schoolNo ,Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findBySchoolNo(schoolNo,pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    public Page<OrderDTO> complexFindList(OrderListForm orderListForm, Pageable pageable) {
        Page<OrderMaster> findResult = orderMasterRepository.findAll(new Specification<OrderMaster>() {
            @Override
            public Predicate toPredicate(Root<OrderMaster> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();

                if (StringUtils.isNoneBlank(orderListForm.getGroupNo())){
                    predicateList.add(cb.like(root.get("groupNo").as(String.class),
                            "%"+ orderListForm.getGroupNo() + "%"));
                }

                if (StringUtils.isNoneBlank(orderListForm.getSchoolNo())){
                    predicateList.add(cb.like(root.get("schoolNo").as(String.class),
                            orderListForm.getSchoolNo()));
                }

                if (StringUtils.isNoneBlank(orderListForm.getOpenid())){
                    predicateList.add(cb.equal(root.get("userOpenid").as(String.class),
                            orderListForm.getOpenid()));
                }

                if (orderListForm.getOrderStatus() != null){
                    predicateList.add(cb.equal(root.get("orderStatus").as(Integer.class),
                            orderListForm.getOrderStatus()));
                }

                if (orderListForm.getPayStatus() != null){
                    predicateList.add(cb.equal(root.get("payStatus").as(Integer.class),
                            orderListForm.getPayStatus()));
                }

                Predicate[] p = new Predicate[predicateList.size()];
                return cb.and(predicateList.toArray(p));
            }
        }, pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(findResult.getContent());

        for (OrderDTO orderDTO :orderDTOList){
            List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderDTO.getOrderId());
            orderDTO.setOrderDetailList(orderDetailList);
        }

        return new PageImpl<OrderDTO>(orderDTOList, pageable, findResult.getTotalElements());
    }

    @Override
    public List<OrderDTO> findByTimeBetween(OrderListForm orderListForm) {
        //如果传入参数为空，则返回一个空集
        if (!StringUtils.isNoneBlank(orderListForm.getStartTime(),orderListForm.getEndTime())){
            List<OrderDTO> orderDTOList = new ArrayList<>();
            return orderDTOList;
        }

        List<OrderMaster> findResult = orderMasterRepository.findAll(new Specification<OrderMaster>() {
            @Override
            public Predicate toPredicate(Root<OrderMaster> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();

                if (StringUtils.isNoneBlank(orderListForm.getStartTime(),orderListForm.getEndTime())){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date startTime;
                    Date endTime;
                    try {
                        startTime = dateFormat.parse(orderListForm.getStartTime());
                        endTime = dateFormat.parse(orderListForm.getEndTime());
                    }catch (ParseException e){
                        startTime = new Date(946656000000L);//2000 01 01
                        endTime = new Date(946656000000L);//2000 01 01
                    }
                    predicateList.add(cb.between(root.<Date>get("createTime"),startTime,endTime));
                }

                if (orderListForm.getPayStatus() != null){
                    predicateList.add(cb.equal(root.get("payStatus").as(Integer.class),
                            orderListForm.getPayStatus()));
                }

                if (StringUtils.isNoneBlank(orderListForm.getSchoolNo())){
                    predicateList.add(cb.like(root.get("schoolNo").as(String.class),
                            orderListForm.getSchoolNo()));
                }

                Predicate[] p = new Predicate[predicateList.size()];
                return cb.and(predicateList.toArray(p));
            }
        });
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(findResult);
        for (OrderDTO orderDTO :orderDTOList){
            List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderDTO.getOrderId());
            orderDTO.setOrderDetailList(orderDetailList);
        }
        return orderDTOList;
    }

    @Override
    public List<OrderDetail> productSalesVolume(OrderListForm orderListForm) {
        // 查询时间段内的订单
        List<OrderDTO> orderDTOList = findByTimeBetween(orderListForm);

        //将非取消且已支付订单Id存入列表
        List<String> orderIdList = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList){
            if ((!orderDTO.getOrderStatus().equals(2))&&orderDTO.getPayStatus().equals(1)){
                orderIdList.add(orderDTO.getOrderId());
            }
        }

        // 查找所有订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderIdIn(orderIdList);

        List<OrderDetail> statisticalResult = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetailList){

            if (CollectionUtils.isEmpty(statisticalResult)){
                statisticalResult.add(orderDetail);
                continue;
            }

            // 如果统计中已存在该商品，则直接商品数量
            // 统计中没有该商品记录，新增记录
            for (int i = 0; i < statisticalResult.size(); i++){

                if (statisticalResult.get(i).getProductId().equals(orderDetail.getProductId())){
                    Integer count = statisticalResult.get(i).getProductQuantity() + orderDetail.getProductQuantity();
                    statisticalResult.get(i).setProductQuantity(count);
                    break;
                }

                if (i == statisticalResult.size()-1){
                    statisticalResult.add(orderDetail);
                    break;
                }
            }
        }

        // 按productQuantity降序、productPrice降序、detailId升序排序
        String [] sortNameArr = {"productQuantity","productPrice","detailId"};
        boolean [] isAscArr = {false,false,true};
        ListUtils.sort(statisticalResult,sortNameArr, isAscArr);

        return statisticalResult;
    }
}
