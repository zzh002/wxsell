package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.PageListVO;
import com.hnust.wxsell.VO.ProductInfoVO;
import com.hnust.wxsell.VO.ProductVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.converter.ReplenishForm2ReplenishDTOConverter;
import com.hnust.wxsell.dataobject.*;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.ReplenishFrom;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.KeyUtil;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/13 0013 18:30
 **/
@RestController
@RequestMapping("/seller/replenish")
@Slf4j
public class SellerReplenishController {
    @Autowired
    private ReplenishService replenishService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private GroupProductService groupProductService;

    /**
     * 配送列表
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
        Page<ReplenishDTO> replenishDTOPage = replenishService.
                findListBySchoolNo(sellerInfo.getSchoolNo(),request);

        pageListVO.setCurrentPage(page);
        pageListVO.setSize(size);
        pageListVO.setList(replenishDTOPage.getContent());

        return ResultVOUtil.success(pageListVO);
    }

    /**
     * 补货详情
     * @param replenishId
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("replenishId") String replenishId){

        ReplenishDTO replenishDTO = replenishService.findOne(replenishId);

        return ResultVOUtil.success(replenishDTO);
    }

    /**
     * 完成补货订单，生成配送单
     * @param replenishFrom
     * @param bindingResult
     * @return
     */
    @PostMapping("/finish")
    public ResultVO finish(@Valid ReplenishFrom replenishFrom,
                           @RequestHeader("token") String token,
                           BindingResult bindingResult,
                           HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", replenishFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        // 1.验证token
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);

        // 2.解析Json数据
        ReplenishDTO replenishDTO = ReplenishForm2ReplenishDTOConverter.convert(replenishFrom);
        //3.查找补货订单并修改商品数量
        List<ReplenishDetail> replenishDetailList = replenishDTO.getReplenishDetailList();
        ReplenishDTO result = replenishService.findOne(replenishDetailList.get(0).getReplenishId());
        if (result==null){
            return ResultVOUtil.error(ResultEnum.REPLENISH_NOT_EXIST.getCode(),
                    ResultEnum.REPLENISH_NOT_EXIST.getMessage());
        }
        List<CartDTO> cartDTOList = replenishDetailList.stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        List<ReplenishDetail> detailList = result.getReplenishDetailList();
        BigDecimal replenishAmount = new BigDecimal(BigInteger.ZERO);
        boolean flag = true;
        for (ReplenishDetail replenishDetail : detailList){
        for (CartDTO cartDTO : cartDTOList){
                if (replenishDetail.getProductId().equals(cartDTO.getProductId())){
                    replenishAmount = replenishDetail.getProductPrice()
                            .multiply(new BigDecimal(cartDTO.getProductQuantity()))
                            .add(replenishAmount);
                    replenishDetail.setProductQuantity(cartDTO.getProductQuantity());
                    replenishService.save(replenishDetail);
                    flag = false;
                    break;
                }
            }
            if (flag){
                ProductDTO productDTO = productService.findOne(replenishDetail.getProductId());
                if (productDTO==null){
                    ResultVOUtil.error(ResultEnum.PRODUCT_NOT_EXIST.getCode(),
                            ResultEnum.PRODUCT_NOT_EXIST.getMessage());
                }
                //删除多余的商品
                replenishService.delete(replenishDetail);
            }
            flag = true;
        }
        result.setReplenishAmount(replenishAmount);
        ReplenishMaster replenishMaster = new ReplenishMaster();
        BeanUtils.copyProperties(result,replenishMaster);
        replenishService.save(replenishMaster);
        result = replenishService.findOne(result.getReplenishId());
        ReplenishDTO replenishDTO1 = replenishService.finish(result);
        if (replenishDTO1==null){
            return ResultVOUtil.error(ResultEnum.REPLENISH_NOT_EXIST.getCode(),
                    ResultEnum.REPLENISH_NOT_EXIST.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 取消补货
     * @param replenishId
     * @return
     */
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("replenishId") String replenishId){

        ReplenishDTO replenishDTO = replenishService.findOne(replenishId);
        ReplenishDTO replenishDTO1 = replenishService.cancel(replenishDTO);
        if (replenishDTO1==null){
            return ResultVOUtil.error(ResultEnum.REPLENISH_NOT_EXIST.getCode(),
                    ResultEnum.REPLENISH_NOT_EXIST.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 补货商品列表
     * @param token
     * @param groupNo
     * @return
     */
    @GetMapping("/productlist")
    public ResultVO productlist(@RequestParam("token") String token,
                         @RequestParam("groupNo") String groupNo) {

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        List<ProductDTO> productDTOList = productService.findUpAll(sellerInfo.getSchoolNo());
        //查询所有的类目
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
                    ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
                    productInfoVO.setProductQuantity(productDistrict.getProductStock());
                    GroupProduct groupProduct = groupProductService.findBySchoolNoAndGroupNoAndProductId
                            (sellerInfo.getSchoolNo(),groupNo,productDTO.getProductId());
                    if (groupProduct!=null){
                        productInfoVO.setProductStock(groupProduct.getProductStock());
                        productInfoVO.setProductSales(groupProduct.getProductSales());
                        productInfoVO.setProductStockout(groupProduct.getProductStockout());
                    }
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
