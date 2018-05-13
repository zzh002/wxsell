package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.SellerLoginForm;
import com.hnust.wxsell.form.SellerRegisterForm;
import com.hnust.wxsell.form.SellerUpdateForm;
import com.hnust.wxsell.service.SellerService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author ZZH
 * @date 2018/4/11 0011 15:02
 **/
@Controller
@RequestMapping("/seller")
@Slf4j
public class SellerController {

    @Autowired
    SellerService sellerService;
    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;


    /**
     * 卖家登陆
     * @param sellerLoginForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public String login(@Valid SellerLoginForm sellerLoginForm,
                        BindingResult bindingResult,
                        HttpServletResponse response){

        SellerInfo sellerInfo = new SellerInfo();
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        //TODO
        if (bindingResult.hasErrors()) {
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
          /*  String url = projectUrlConfig.getWechatMpAuthorize()
                    +"/sell/wechat/authorize?returnUrl="
                    +projectUrlConfig.getSell()+"";
            return "redirect:"+url;*/
        }

        try {
            sellerInfo = sellerService.sellerlogin(sellerLoginForm);
        }catch (SellException e){
            log.error(e.getMessage());
          /*  String url = projectUrlConfig.getWechatMpAuthorize()
                    +"/sell/wechat/authorize?returnUrl="
                    +projectUrlConfig.getSell()+"";
            return "redirect:"+url;*/
          throw new SellException(e.getCode(),e.getMessage());
        }
        return "redirect:" + projectUrlConfig.getSell()
                + "/sell/seller/admin?openid=" + sellerInfo.getOpenid();
    }

    /**
     * 卖家注册
     * @param sellerRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid SellerRegisterForm sellerRegisterForm,
                          BindingResult bindingResult){

        SellerInfo sellerInfo = new SellerInfo();

        if (bindingResult.hasErrors()) {
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            sellerInfo = sellerService.save(sellerRegisterForm);
        }catch (SellException e){
            log.error(e.getMessage());
           throw new SellException(e.getCode(),e.getMessage());
        }
          return "redirect:" + projectUrlConfig.getSell()
                + "/sell/seller/admin?openid=" + sellerInfo.getOpenid();
       /* return "redirect:" + "http://localhost:8080"
                + "/wxsell/seller/admin?openid=" + sellerInfo.getOpenid();*/
    }

    /**
     * 修改密码
     * @param sellerUpdateForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultVO update(@Valid SellerUpdateForm sellerUpdateForm,
                         BindingResult bindingResult){

        SellerInfo sellerInfo = new SellerInfo();

        if (bindingResult.hasErrors()) {
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            sellerInfo = userTokenService.getSellerInfo(sellerUpdateForm.getToken());
            if (!sellerInfo.getPassword().equals(sellerUpdateForm.getPasswordOld())){
                throw new SellException(ResultEnum.SELLER_UPDATE_ERROR.getCode(),
                        ResultEnum.SELLER_UPDATE_ERROR.getMessage());
            }
            sellerInfo.setPassword(sellerUpdateForm.getPasswordNew());
            sellerService.update(sellerInfo);
        }catch (SellException e){
            log.error(e.getMessage());
            throw new SellException(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

}
