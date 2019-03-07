package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ProductInfoVO;
import com.hnust.wxsell.VO.ProductVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.dataobject.*;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.ProductForm;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.MathUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import com.hnust.wxsell.utils.UploadFileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/12 0012 21:08
 **/
@CrossOrigin
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService categoryService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private GroupProductService groupProductService;

    @Autowired
    private GroupMasterService groupMasterService;

    /**
     * 商品列表
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("token") String token) {

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        List<ProductDTO> productDTOList = productService.findAll(sellerInfo.getSchoolNo());
        //查询所有的类目
        List<ProductCategory> productCategoryList = categoryService.findAll();

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
                    ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
                    productInfoVO.setProductStock(productDistrict.getProductStock());
                    productInfoVO.setProductStatus(productDistrict.getProductStatus());
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }


    /**
     * 商品上架
     * @param productId
     * @param token
     * @return
     */
    @RequestMapping("/on_sale")
    public ResultVO onSale(@RequestParam("productId") String productId,
                           @RequestParam("token") String token) {
        try {
            SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
            productService.onSale(productId,sellerInfo.getSchoolNo());
        } catch (SellException e) {
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 商品下架
     * @param productId
     * @param token
     * @return
     */
    @RequestMapping("/off_sale")
    public ResultVO offSale(@RequestParam("productId") String productId,
                            @RequestParam("token") String token) {
        try {
            SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
            productService.offSale(productId,sellerInfo.getSchoolNo());
        } catch (SellException e) {
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     *商品信息
     * @param productId
     * @param token
     * @return
     */
    @GetMapping("/index")
    public ResultVO index(@RequestParam(value = "productId", required = false) String productId,
                          @RequestParam("token") String token) {
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (!StringUtils.isEmpty(productId)) {
            ProductDTO productDTO = productService.findOne(productId,sellerInfo.getSchoolNo());
            ProductVO productVO = new ProductVO();
            ProductInfoVO productInfoVO = new ProductInfoVO();
            BeanUtils.copyProperties(productDTO,productInfoVO);
            ProductDistrict productDistrict = productDTO.getProductDistrictList().get(0);
            productInfoVO.setProductStock(productDistrict.getProductStock());
            productInfoVO.setProductStatus(productDistrict.getProductStatus());
            productVO.setCategoryType(productDTO.getCategoryType());
            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            productInfoVOList.add(productInfoVO);
            productVO.setProductInfoVOList(productInfoVOList);
            return ResultVOUtil.success(productVO);
        }
        return ResultVOUtil.success();
    }

    /**
     * 更新/保存
     * @param file
     * @param form
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@RequestParam(value = "file", required = false) MultipartFile file,
                         @Valid ProductForm form,
                         BindingResult bindingResult,
                         HttpServletRequest request) {
        SellerInfo sellerInfo = userTokenService.getSellerInfo(form.getToken());
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(1003,"参数缺失");
        }

        // 处理上传图片
        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
        if (file!=null){
            try {
                String imgUrl = UploadFileUtil.uploadProductImg(file, filePath, form.getProductName());
                form.setProductIcon(projectUrlConfig.getSell()+imgUrl);
            } catch (Exception e) {
                return ResultVOUtil.error(e.hashCode(),e.getMessage());
            }
        }

        ProductDTO productDTO = new ProductDTO();
        ProductDistrict productDistrict = new ProductDistrict();
        try {
            //如果productId为空, 说明是新增
            if (!StringUtils.isEmpty(form.getProductId())) {
                productDTO = productService.findOne(form.getProductId(),sellerInfo.getSchoolNo());
                productDistrict = productDTO.getProductDistrictList().get(0);
            } else {
                form.setProductId(KeyUtil.genUniqueKey());
                productDistrict.setId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(form, productDTO);

            BeanUtils.copyProperties(form,productDistrict);
            productDistrict.setSchoolNo(sellerInfo.getSchoolNo());
            List<ProductDistrict> productDistrictList = new ArrayList<>();
            productDistrictList.add(productDistrict);
            productDTO.setProductDistrictList(productDistrictList);
            productService.save(productDTO);
            List<GroupProduct> groupProductList = groupProductService.
                    findByProductIdAndSchoolNo(form.getProductId(),sellerInfo.getSchoolNo());
            for (GroupProduct groupProduct : groupProductList){
                if (!MathUtil.equals(groupProduct.getProductPrice().doubleValue(), form.getProductPrice().doubleValue())){
                    GroupMaster groupMaster = groupMasterService.findBySchoolAndGroupNo
                            (groupProduct.getSchoolNo(),groupProduct.getGroupNo());
                    BigDecimal amount = form.getProductPrice().subtract(groupProduct.getProductPrice());
                    BigDecimal quantity = new BigDecimal(groupProduct.getProductStock().toString());
                    amount = amount.multiply(quantity);
                    groupMaster.setGroupAmount(groupMaster.getGroupAmount().add(amount));
                    groupMasterService.save(groupMaster);
                }
                Integer stock = groupProduct.getProductStock();
                BeanUtils.copyProperties(form,groupProduct);
                groupProduct.setProductStock(stock);
                groupProductService.save(groupProduct);

            }


        } catch (SellException e) {
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

}
