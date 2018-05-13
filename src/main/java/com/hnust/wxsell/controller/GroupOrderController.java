package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.converter.OrderForm2OrderDTOConverter;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.OrderForm;
import com.hnust.wxsell.service.BuyerService;
import com.hnust.wxsell.service.OrderService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:18
 **/
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class GroupOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private UserTokenService userTokenService;


    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
                                                BindingResult bindingResult,
                                                @RequestParam("token") String token,
                                                HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        // 验证token,并查找返回用户信息
        UserMaster userMaster = userTokenService.getUserMaster(token);

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        orderDTO.setGroupNo(userMaster.getGroupNo());
        orderDTO.setUserOpenid(userMaster.getOpenId());
        orderDTO.setSchoolNo(userMaster.getSchoolNo());

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestHeader("token") String token,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {

        // 验证token,并查找返回用户信息
        UserMaster userMaster = userTokenService.getUserMaster(token);

        PageRequest request = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(userMaster.getOpenId(), request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestHeader("token") String token,
                                     @RequestParam("orderId") String orderId) {

        // 验证token,并查找返回用户信息
        UserMaster userMaster = userTokenService.getUserMaster(token);

        OrderDTO orderDTO = buyerService.findOrderOne(userMaster.getOpenId(), orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestHeader("token") String token,
                           @RequestParam("orderId") String orderId) {

        // 验证token,返回openid
        UserMaster userMaster = userTokenService.getUserMaster(token);

        buyerService.cancelOrder(userMaster.getOpenId(), orderId);
        return ResultVOUtil.success();
    }
}
