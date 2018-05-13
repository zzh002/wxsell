package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.*;
import com.hnust.wxsell.dataobject.*;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.enums.PayStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.OrderListForm;
import com.hnust.wxsell.service.OrderService;
import com.hnust.wxsell.service.ProductCategoryService;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import com.hnust.wxsell.utils.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/12 0012 19:35
 **/
@RestController
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 订单列表
     * @param page
     * @param size
     * @param orderListForm
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                         @Valid OrderListForm orderListForm) {

        SellerInfo sellerInfo = userTokenService.getSellerInfo(orderListForm.getToken());
        orderListForm.setSchoolNo(sellerInfo.getSchoolNo());
        //设置定单排序方式，这里是按时间降序排列
        PageRequest request = new PageRequest(page - 1, size,
                SortUtil.basicSort());
        BigDecimal orderListAmount = new BigDecimal(BigInteger.ZERO);

       OrderVO orderVO = new OrderVO();
        try {
            Page<OrderDTO> orderDTOPage = orderService.complexFindList(orderListForm,request);
            List<OrderDTOVO> orderDTOVOList = new ArrayList<>();
            for (OrderDTO orderDTO : orderDTOPage.getContent()){
                OrderDTOVO orderDTOVO = new OrderDTOVO();
                BeanUtils.copyProperties(orderDTO,orderDTOVO);
                orderListAmount = orderDTO.getOrderAmount().add(orderListAmount);
                List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
                for (OrderDetail orderDetail : orderDTO.getOrderDetailList()){
                    OrderDetailVO orderDetailVO = new OrderDetailVO();
                    BeanUtils.copyProperties(orderDetail,orderDetailVO);
                    orderDetailVOList.add(orderDetailVO);
                }
                orderDTOVO.setOrderDetailVOList(orderDetailVOList);
                orderDTOVOList.add(orderDTOVO);
            }
            /** 计算订单列表数量 */
            Integer orderListSize = orderDTOPage.getContent().size();
            orderVO.setSize(size);
            orderVO.setCurrentPage(page);
            orderVO.setOrderDTOVOList(orderDTOVOList);
            orderVO.setOrderListSize(orderListSize);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }


        return ResultVOUtil.success(orderVO);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("orderId") String orderId) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("orderId") String orderId) {

            OrderDTO orderDTO = orderService.findOne(orderId);
            if (orderDTO==null){
                return ResultVOUtil.error(ResultEnum.ORDER_DETAIL_EMPTY.getCode(),
                        ResultEnum.ORDER_DETAIL_EMPTY.getMessage());
            }
        return new ResultVOUtil().success(orderDTO);
    }

    /**
     * 完成订单
     * @param orderId
     * @return
     */
    @GetMapping("/finish")
    public ResultVO finished(@RequestParam("orderId") String orderId) {

        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 查询最近订单列表
     * @param orderListForm
     * @return
     */
    @GetMapping("/timeBetween")
    public ResultVO timeBetween(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Valid OrderListForm orderListForm) {

        BigDecimal orderListAmount = new BigDecimal(BigInteger.ZERO);
        SellerInfo sellerInfo = userTokenService.getSellerInfo(orderListForm.getToken());
        orderListForm.setSchoolNo(sellerInfo.getSchoolNo());
        //设置按查询时间内所有支付的订单
            orderListForm.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        List<OrderDTO> orderDTOList = orderService.findByTimeBetween(orderListForm);
        /**计算所有订单总价 */
        for (OrderDTO orderDTO: orderDTOList){
            orderListAmount = orderDTO.getOrderAmount().add(orderListAmount);
        }
        /** 计算订单列表数量 */
        Integer orderListSize = orderDTOList.size();
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderListAmount(orderListAmount);
        orderVO.setOrderListSize(orderListSize);
        orderVO.setCurrentPage(page);
        orderVO.setSize(size);
        List<OrderDTOVO> orderDTOVOList = new ArrayList<>();
        for (OrderDTO orderDTO : orderDTOList){
            OrderDTOVO orderDTOVO = new OrderDTOVO();
            BeanUtils.copyProperties(orderDTO,orderDTOVO);
           List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDTO.getOrderDetailList()){
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                BeanUtils.copyProperties(orderDetail,orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }
            orderDTOVO.setOrderDetailVOList(orderDetailVOList);
            orderDTOVOList.add(orderDTOVO);
        }
        orderVO.setOrderDTOVOList(orderDTOVOList);

        return ResultVOUtil.success(orderVO);
    }

    /**
     * 查询商品销售情况
     * @param orderListForm
     * @return
     */
    @GetMapping("/productSalesVolume")
    public ResultVO productSalesVolume(@Valid OrderListForm orderListForm){

        SellerInfo sellerInfo = userTokenService.getSellerInfo(orderListForm.getToken());
        orderListForm.setSchoolNo(sellerInfo.getSchoolNo());
        //设置按查询时间内所有支付的订单
        orderListForm.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        List<OrderDetail> orderDetailList = orderService.productSalesVolume(orderListForm);
        List<CartDTO> cartDTOList = orderDetailList.stream()
                .map(e ->new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        List<String> productIdList = orderDetailList.stream()
                .map(e ->e.getProductId())
                .collect(Collectors.toList());
        List<ProductDTO> productDTOList = productService.findByProductStatusAndProductIdIn
                (sellerInfo.getSchoolNo(),productIdList);
        List<Integer> categoryTypeList = productDTOList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(categoryTypeList);
      //3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductDTO productDTO: productDTOList) {
                if (productDTO.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productDTO, productInfoVO);
                    productInfoVO.setPurchasePrice(null);
                    for (CartDTO cartDTO : cartDTOList){
                        if (cartDTO.getProductId().equals(productDTO.getProductId()))
                        {
                            productInfoVO.setProductQuantity(cartDTO.getProductQuantity());
                        }
                    }
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }

}
