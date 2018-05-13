package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.PageListVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.converter.ReplenishForm2ReplenishDTOConverter;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dto.DispatchDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.ReplenishFrom;
import com.hnust.wxsell.service.DispatchService;
import com.hnust.wxsell.service.TemplateService;
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
import java.util.Map;

/**
 * @author ZZH
 * @date 2018/4/13 0013 19:38
 **/
@RestController
@RequestMapping("/seller/dispatch")
@Slf4j
public class SellerDispatchController {
    @Autowired
    DispatchService dispatchService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private TemplateService templateService;

    /**
     *配送列表
     * @param page
     * @param size
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                         @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        PageListVO pageListVO = new PageListVO();

        PageRequest request = new PageRequest(page - 1, size);
        Page<DispatchDTO> dispatchDTOPage = dispatchService.
                findList(sellerInfo.getSchoolNo(),request);

        pageListVO.setCurrentPage(page);
        pageListVO.setSize(size);
        pageListVO.setList(dispatchDTOPage.getContent());

        return ResultVOUtil.success(pageListVO);
    }

    /**
     * 取消配送
     * @param groupNo
     * @param token
     * @return
     */
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("groupNo") String groupNo,
                           @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        DispatchDTO dispatchDTO = dispatchService.findOne(sellerInfo.getSchoolNo(),groupNo);
        dispatchService.cancel(dispatchDTO);

        return ResultVOUtil.success();
    }

    /**
     * 配送单详情
     * @param groupNo
     * @param token
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("groupNo") String groupNo,
                           @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        DispatchDTO dispatchDTO = dispatchService.findOne(sellerInfo.getSchoolNo(),groupNo);

        return ResultVOUtil.success(dispatchDTO);
    }

    /**
     * 完成配送单
     * @param groupNo
     * @param token
     * @return
     */
    @GetMapping("/finish")
    public ResultVO finish(@RequestParam("groupNo") String groupNo,
                           @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        dispatchService.finish(sellerInfo.getSchoolNo(),groupNo);

        return ResultVOUtil.success();

    }

    /**
     * 创建配送模板
     * @param groupNo
     * @param token
     * @return
     */
    @GetMapping("/create_template")
    public ResultVO create_template(@RequestParam("groupNo") String groupNo,
                                    @RequestParam("token") String token){

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        try {
            DispatchDTO dispatchDTO = dispatchService.findOne(sellerInfo.getSchoolNo(),groupNo);
            templateService.save(dispatchDTO);
        }catch (SellException e) {
            log.error("【卖家端完结补货】发生异常{}", e);
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return ResultVOUtil.success();
    }

    /**
     * 创建配送单
     * @param replenishFrom
     * @param token
     * @param bindingResult
     * @return
     */
    @RequestMapping("/create")
    public ResultVO create(@Valid ReplenishFrom replenishFrom,
                           @RequestParam("token") String token,
                           BindingResult bindingResult,
                           HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", replenishFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        ReplenishDTO replenishDTO = ReplenishForm2ReplenishDTOConverter.convert(replenishFrom);

        if (CollectionUtils.isEmpty(replenishDTO.getReplenishDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        replenishDTO.setSchoolNo(sellerInfo.getSchoolNo());
        replenishDTO.setGroupNo(replenishFrom.getGroupNo());
        ReplenishDTO addResult = dispatchService.add(replenishDTO);

        Map<String, String> map = new HashMap<>();
        map.put("groupNo", addResult.getGroupNo());

        return ResultVOUtil.success(map);
    }
}
