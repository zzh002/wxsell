package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ProductInfoVO;
import com.hnust.wxsell.VO.ProductVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.ProductCategory;
import com.hnust.wxsell.dataobject.ProductDistrict;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.service.ProductCategoryService;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.SchoolService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
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
 * @date 2018/4/6 0006 15:10
 **/
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private UserTokenService userTokenService;

    /**
     * 商品类目列表
     * @return
     */
    @GetMapping("/category")
    public ResultVO category(@RequestParam("token") String token){

        // token身份确认
        UserMaster userMaster = userTokenService.getUserMaster(token);
//        1。查询所有上架商品
        List<ProductDTO> productDTOList = productService.findUpAll(userMaster.getSchoolNo());

//        2。查询类目（一次性查询）
//        List<Integer> categoryTypeList = new ArrayList<>();
//        传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
//        精简方法(java8, lambda)
        List<Integer> categoryTypeList = productDTOList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(categoryTypeList);

        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList){
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productCategory,productVO);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }


    /**
     * 买家补货商品列表（单类目商品）
     * @param type
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("type") Integer type ,
                         @RequestParam("token") String token){
        // token身份确认
        UserMaster userMaster = userTokenService.getUserMaster(token);
//        1。查询左右上架商品
        List<ProductDTO> productDTOList = productService.findUpAll(userMaster.getSchoolNo());

        List<ProductInfoVO> productInfoVOList = new ArrayList<>();
        for (ProductDTO productDTO: productDTOList) {
            if (productDTO.getCategoryType().equals(type)) {
                ProductInfoVO productInfoVO = new ProductInfoVO();
                BeanUtils.copyProperties(productDTO, productInfoVO);
                productInfoVO.setPurchasePrice(null);
            //    ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
          //      productInfoVO.setProductStock(productDistrict.getProductStock());
                productInfoVOList.add(productInfoVO);
            }
        }
        return ResultVOUtil.success(productInfoVOList);
    }

    /**
     * 商品详情
     * @param productId
     * @return
     */
    @GetMapping("/info")
    public ResultVO info(@RequestParam("productId") String productId){

        ProductDTO productDTO = productService.findOne(productId);

        ProductInfoVO productInfoVO = new ProductInfoVO();
        BeanUtils.copyProperties(productDTO,productInfoVO);
        ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
        productInfoVO.setProductStock(productDistrict.getProductStock());
        return ResultVOUtil.success(productInfoVO);
    }

    /**
     * 买家补货商品列表（所有类目商品）
     * @return
     */
    @GetMapping("/WxList")
    public ResultVO WxList(@RequestParam("token") String token) {
        // token身份确认
        UserMaster userMaster = userTokenService.getUserMaster(token);
        //1. 查询所有的上架商品
        List<ProductDTO> productDTOList = productService.findUpAll(userMaster.getSchoolNo());

        //2. 查询类目(一次性查询)
//        List<Integer> categoryTypeList = new ArrayList<>();
        //传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //精简方法(java8, lambda)
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
                 // ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
               //   productInfoVO.setProductStock(productDistrict.getProductStock());
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
