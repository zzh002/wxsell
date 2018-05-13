package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.UserDetail;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.UserDetailFrom;
import com.hnust.wxsell.service.UserDetailService;
import com.hnust.wxsell.service.UserMasterService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author ZZH
 * @date 2018/4/5 0005 12:57
 **/
@RestController
@RequestMapping("/buyer/user")
@Slf4j
public class BuyerUserDetailController {
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserMasterService userMasterService;


    @PostMapping(value = "/address/create")
    public ResultVO addressCreate(@Valid  UserDetailFrom userDetailFrom,
                                  BindingResult bindingResult,
                                  @RequestParam("token") String token,
                                  HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        // 1.验证地址参数
        if (bindingResult.hasErrors()) {
            log.error("【保存用户地址】参数不正确, userDetailFrom={}", userDetailFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        // 2.token身份确认，取出tokenValue值
        String tokenValue = userTokenService.getTokenValue(token);

        // 3.tokenValue查找数据库是否有该用户地址信息
        UserMaster userMaster = userMasterService.findByOpenid(tokenValue);
        if(userMaster == null){
            throw new SellException(ResultEnum.USER_NOT_EXIST);
        }

        // 4.根据用户信息是否存在，从而判断是添加地址还是更新地址
        UserDetail userDetail = userDetailService.findByOpenId(tokenValue);
        if (userDetail==null){
            userDetail = new UserDetail();
            userDetail.setId(KeyUtil.genUniqueKey());
            userDetail.setUserId(userMaster.getUserId());
            userDetail.setOpenId(tokenValue);
        }
        BeanUtils.copyProperties(userDetailFrom,userDetail);
        userDetailService.save(userDetail);

        return ResultVOUtil.success();
    }

    @GetMapping("/address/detail")
    public ResultVO addressDetail(@RequestParam("token") String token){

        // 1.token身份确认，取出tokenValue值
        String tokenValue = userTokenService.getTokenValue(token);

        UserDetail userDetail = userDetailService.findByOpenId(tokenValue);
        if(userDetail==null){
            throw new SellException(ResultEnum.USER_ADDRESS_NOT_EXIST);
        }
        return ResultVOUtil.success(userDetail);
    }
}
