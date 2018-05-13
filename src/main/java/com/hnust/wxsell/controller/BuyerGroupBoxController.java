package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.BoxApply;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.BoxApplyForm;
import com.hnust.wxsell.service.BoxApplyService;
import com.hnust.wxsell.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author ZZH
 * @date 2018/4/3 0003 21:46
 **/
@RestController
@RequestMapping("/group/buyer")
public class BuyerGroupBoxController {

    @Autowired
    BoxApplyService boxApplyService;

    @PostMapping("/boxApply")
    public ResultVO boxApply(@Valid BoxApplyForm boxApplyForm,
                             BindingResult bindingResult,
                             HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()){
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        BoxApply boxApply = boxApplyService.create(boxApplyForm);

        return ResultVOUtil.success();
    }
}
