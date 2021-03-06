package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.converter.ReplenishForm2ReplenishDTOConverter;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.UserDetail;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.ReplenishFrom;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
 * @date 2018/4/8 0008 21:30
 **/
@CrossOrigin
@RestController
@RequestMapping("/buyer/replenish")
@Slf4j
public class GroupReplenishController {

    @Autowired
    ReplenishService replenishService;

    @Autowired
    UserMasterService userMasterService;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    UserDetailService userDetailService;


    @Autowired
    GroupMasterService groupMasterService;

    @PostMapping("/MaCreate")
    public ResultVO<Map<String, String>> MaCreate(@Valid ReplenishFrom replenishFrom,
                                                  @RequestParam("token") String token,
                                                  BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", replenishFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        // 1.验证token
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);

        // 2.解析Json数据
        ReplenishDTO replenishDTO = ReplenishForm2ReplenishDTOConverter.convert(replenishFrom);

        if(CollectionUtils.isEmpty(replenishDTO.getReplenishDetailList())){
            log.error("【创建订单】 购物车不能为空");
            throw new SellException(ResultEnum.REPLENISH_NOT_EXIST);
        }
        //查找相应寝室
        GroupMasterDTO groupMasterDTO = groupMasterService.
                findByGroupNoAndSchoolNo(replenishFrom.getGroupNo(),sellerInfo.getSchoolNo());

        replenishDTO.setSchoolNo(groupMasterDTO.getSchoolNo());
        replenishDTO.setGroupNo(groupMasterDTO.getGroupNo());
        replenishDTO.setUserName(groupMasterDTO.getUserName());
        replenishDTO.setUserPhone(groupMasterDTO.getUserPhone());
        replenishDTO.setOpenId("seller");
        ReplenishDTO createResult = replenishService.create(replenishDTO);

        Map<String, String> map = new HashMap<>();
        map.put("replenishId", createResult.getReplenishId());

        return ResultVOUtil.success(map);
    }

    @PostMapping("/WxCreate")
    public ResultVO<Map<String, String>> WxCreate(@Valid ReplenishFrom replenishFrom,
                                                  @RequestParam("token") String token,
                                                  BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", replenishFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        // 1.验证token
        UserMaster userMaster = userTokenService.getUserMaster(token);

        // 2.解析Json数据
        ReplenishDTO replenishDTO = ReplenishForm2ReplenishDTOConverter.convert(replenishFrom);

        //查找相应寝室
        GroupMasterDTO groupMasterDTO = groupMasterService.
                findByGroupNoAndSchoolNo(userMaster.getGroupNo(),userMaster.getSchoolNo());

        replenishDTO.setSchoolNo(groupMasterDTO.getSchoolNo());
        replenishDTO.setGroupNo(groupMasterDTO.getGroupNo());
        replenishDTO.setUserName(groupMasterDTO.getUserName());
        replenishDTO.setUserPhone(groupMasterDTO.getUserPhone());
        replenishDTO.setOpenId(userMaster.getOpenId());

        ReplenishDTO createResult = replenishService.create(replenishDTO);

        Map<String, String> map = new HashMap<>();
        map.put("replenishId", createResult.getReplenishId());

        return ResultVOUtil.success(map);
    }

    //补货定单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         @RequestHeader("token") String token) {

        // 1.验证token
        UserMaster userMaster = userTokenService.getUserMaster(token);

        PageRequest request = new PageRequest(page-1, size);

        Page<ReplenishDTO> replenishDTOPage = replenishService.findList(userMaster.getOpenId(), request);

        return ResultVOUtil.success(replenishDTOPage.getContent());
    }

    //定单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestHeader("token") String token,
                                     @RequestParam("orderId") String orderId) {

        // 1.验证token
        UserMaster userMaster = userTokenService.getUserMaster(token);

        ReplenishDTO replenishDTO = replenishService.findOne(orderId);

        if(!replenishDTO.getOpenId().equals(userMaster.getOpenId()) ){
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }

        return ResultVOUtil.success(replenishDTO);
    }
}
