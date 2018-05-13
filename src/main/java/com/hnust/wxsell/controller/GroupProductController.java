package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.GroupProductInfoVO;
import com.hnust.wxsell.VO.GroupProductVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dataobject.ProductCategory;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.service.GroupProductService;
import com.hnust.wxsell.service.ProductCategoryService;
import com.hnust.wxsell.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/7 0007 18:59
 **/
@RestController
@RequestMapping("/group/product")
@Slf4j
public class GroupProductController {
    @Autowired
    private GroupProductService groupProductService;

    @Autowired
    private ProductCategoryService categoryService;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 寝室商品列表
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("token") String token){

        // token身份确认
        UserMaster userMaster = userTokenService.getUserMaster(token);

        //查商品
        List<GroupProduct> groupProductList = groupProductService.
                findBySchoolNoAndGroupNo(userMaster.getSchoolNo(),userMaster.getGroupNo());

        //查询类目（一次性查询）
//        List<Integer> categoryList = new ArrayList<>();
        List<Integer> categoryList = groupProductList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryList);

        //数据拼装
        List<GroupProductVO> groupProductVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList){
            GroupProductVO groupProductVO = new GroupProductVO();
            groupProductVO.setCategoryType(productCategory.getCategoryType());
            groupProductVO.setCategoryName(productCategory.getCategoryName());

            List<GroupProductInfoVO> groupProductInfoVOList = new ArrayList<>();
            for (GroupProduct groupProduct: groupProductList){
                if (groupProduct.getCategoryType().equals(productCategory.getCategoryType())){
                    GroupProductInfoVO groupProductInfoVO = new GroupProductInfoVO();
                    BeanUtils.copyProperties(groupProduct, groupProductInfoVO);
                    groupProductInfoVOList.add(groupProductInfoVO);
                }
            }
            groupProductVO.setGroupProductInfoVOList(groupProductInfoVOList);

            groupProductVOList.add(groupProductVO);
        }

        ResultVO resultVO = new ResultVO();
        resultVO.setData(groupProductVOList);
        resultVO.setCode(0);
        resultVO.setMsg(userMaster.getGroupNo());

        return resultVO;
    }
}
