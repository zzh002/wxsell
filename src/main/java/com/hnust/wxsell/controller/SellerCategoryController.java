package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.ProductCategory;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.CategoryForm;
import com.hnust.wxsell.service.ProductCategoryService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/11 0011 20:19
 **/
@RestController
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private ProductCategoryService categoryService;
    @Autowired
    private UserTokenService userTokenService;

    /**
     * 类目列表
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("token") String token) {
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_NOT_EXIST);
        }
        List<ProductCategory> categoryList = categoryService.findAll();
        return ResultVOUtil.success(categoryList);
    }

    /**
     * 展示
     * @param categoryId
     * @return
     */
    @GetMapping("/index")
    public ResultVO index(@RequestParam(value = "categoryId", required = false) Integer categoryId) {
        if (categoryId != null) {
            ProductCategory productCategory = categoryService.findOne(categoryId);
           return ResultVOUtil.success(productCategory);
        }
        List<ProductCategory> categoryList = categoryService.findAll();
        return ResultVOUtil.success(categoryList);
    }

    /**
     * 保存更新
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@Valid CategoryForm form,
                         BindingResult bindingResult,
                         HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(1003,"缺失参数");
        }

        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId() != null) {
                productCategory = categoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, productCategory);
            categoryService.save(productCategory);
        } catch (SellException e) {
            ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return ResultVOUtil.success();
    }
}
