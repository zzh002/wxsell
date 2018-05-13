package com.hnust.wxsell.handler;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.exception.SellerAuthorizeException;
import com.hnust.wxsell.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ZZH
 * @date 2018/4/11 0011 17:07
 **/
@ControllerAdvice
public class SellExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public String handlerAuthorizeException() {
        //TODO
        String url = projectUrlConfig.getWechatMpAuthorize()
                +"/sell/wechat/authorize?returnUrl="
                +projectUrlConfig.getSell()+"";
        return "redirect:"+url;
    }

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVO handlerSellerException(SellException e){
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
