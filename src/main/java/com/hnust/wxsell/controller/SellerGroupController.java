package com.hnust.wxsell.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnust.wxsell.VO.GroupProductInfoVO;
import com.hnust.wxsell.VO.GroupProductVO;
import com.hnust.wxsell.VO.PageListVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.converter.StockOutFrom2GroupMasterDTOConverter;
import com.hnust.wxsell.dataobject.*;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.GroupMasterForm;
import com.hnust.wxsell.form.StockOutForm;
import com.hnust.wxsell.service.*;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import com.hnust.wxsell.utils.SortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/11 0011 20:46
 **/
@RestController
@RequestMapping("/seller/group")
@Slf4j
public class SellerGroupController {

    @Autowired
    private GroupMasterService groupMasterService;

    @Autowired
    private GroupDistrictService districtService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private GroupProductService groupProductService;
    @Autowired
    private ProductCategoryService categoryService;
    @Autowired
    private ProductService productService;

    /**
     * 单区寝室列表
     * @param groupDistrict
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                         @RequestParam(value = "groupDistrict", defaultValue = "1") String groupDistrict,
                         @RequestParam("token") String token){

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        //设置排序方式,寝室剩余商品金额升序排列
        Sort sort = SortUtil.basicSort("asc", "groupNo");
        PageListVO pageListVO = new PageListVO();

        PageRequest request = new PageRequest(page - 1, size,sort);
        Page<GroupMaster> groupMasterPage = groupMasterService.findByGroupDistrictAndSchoolNo
                (groupDistrict,sellerInfo.getSchoolNo(), request);
      //  List<GroupDistrict> groupDistrictList = districtService.findAll(sellerInfo.getSchoolNo());
        pageListVO.setCurrentPage(page);
        pageListVO.setSize(size);
        pageListVO.setList(groupMasterPage.getContent());

        return ResultVOUtil.success(pageListVO);
    }

    /**
     * 寝室分区
     * @param token
     * @return
     */
    @GetMapping("/district")
    public ResultVO district(@RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        List<GroupDistrict> groupDistrictList = districtService.findAll(sellerInfo.getSchoolNo());
        return ResultVOUtil.success(groupDistrictList);
    }

    /**
     * 寝室补货快照
     * @param groupNo
     * @param token
     * @return
     */
    @RequestMapping("/detail")
    public ResultVO detail(@RequestParam("groupNo")String groupNo,
                           @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        GroupMasterDTO groupMasterDTO = new GroupMasterDTO();
        List<DispatchDetail> snapItems = new ArrayList<>();
        Gson gson = new Gson();

        try {
            groupMasterDTO = groupMasterService.findByGroupNoAndSchoolNo
                    (groupNo,sellerInfo.getSchoolNo());

            //将快照json数据装换成对象
            snapItems = gson.fromJson(groupMasterDTO.getSnapItems(),
                    new TypeToken<List<DispatchDetail>>() {
                    }.getType());
        }catch (SellException e){
            log.error("【寝室商品详情查询】 发生异常{}",e);
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success(snapItems);
    }

    /**
     * 寝室模糊搜索
     * @param token
     * @param groupNo
     * @return
     */
    @GetMapping("/searchList")
    public ResultVO searchList(@RequestParam("token") String token,
                               @RequestParam("groupNo") String groupNo){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        List<GroupMaster> groupMasterList = groupMasterService.findByGroupNo
                (sellerInfo.getSchoolNo(),groupNo);
        return ResultVOUtil.success(groupMasterList);
    }

    /**
     * 展示
     * @param id
     * @return
     */
    @GetMapping("/index")
    public ResultVO index(@RequestParam(value = "groupId", required = false) String id){

        if (!StringUtils.isEmpty(id)) {
            GroupMaster groupMaster = groupMasterService.findOne(id);
            return ResultVOUtil.success(groupMaster);
        }

        return ResultVOUtil.success();
    }

    /**
     * 保存更新
     * @param groupMasterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@Valid GroupMasterForm groupMasterForm,
                         @RequestParam("token") String token,
                         BindingResult bindingResult,
                         HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(1003,"参数错误");
        }

        GroupMaster groupMaster = new GroupMaster();
        try {
//                如果id为空，说明是新增的
            if(!StringUtils.isEmpty(groupMasterForm.getGroupId())){
                groupMaster = groupMasterService.findOne(groupMasterForm.getGroupId());
            }else {
                groupMasterForm.setSchoolNo(sellerInfo.getSchoolNo());
                groupMasterForm.setGroupId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(groupMasterForm, groupMaster);

            groupMasterService.save(groupMaster);
        }catch (SellException e){

            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return ResultVOUtil.success();
    }

    /**
     * 寝室消费排行
     * @param page
     * @param size
     * @param orderField
     * @param token
     * @return
     */
    @GetMapping("/salesList")
    public ResultVO salesList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "50") Integer size,
                             @RequestParam(value = "orderField", defaultValue = "groupAmount") String orderField,
                         @RequestParam("token") String token){

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        //设置排序方式,寝室剩余商品金额升序排列
        PageRequest request = new PageRequest(page - 1, size,
                SortUtil.basicSort("asc",orderField));
        if (orderField.equals("groupConsume")){
            request = new PageRequest(page - 1, size,
                    SortUtil.basicSort("desc",orderField));
        }

        Page<GroupMasterDTO> groupMasterDTOPage = groupMasterService.findList(sellerInfo.getSchoolNo(),request);

        PageListVO pageListVO = new PageListVO();
        pageListVO.setCurrentPage(page);
        pageListVO.setSize(size);
        pageListVO.setList(groupMasterDTOPage.getContent());

        return ResultVOUtil.success(pageListVO);
    }


    /**
     * 寝室商品列表
     * @param token
     * @param groupNo
     * @return
     */
    @GetMapping("/productList")
    public ResultVO productlist(@RequestParam("token") String token,
                         @RequestParam("groupNo") String groupNo){

        // token身份确认
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);

        //查商品
        List<GroupProduct> groupProductList = groupProductService.
                findBySchoolNoAndGroupNo(sellerInfo.getSchoolNo(),groupNo);

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

        return ResultVOUtil.success(groupProductVOList);
    }

    /**
     * 回收商品
     * @param stockOutForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/stockout")
    public ResultVO stockout(@Valid StockOutForm stockOutForm,
                             @RequestParam("token") String token,
                             BindingResult bindingResult,
                             HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()) {
            log.error("【回收商品】参数不正确, stockOutForm={}", stockOutForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        //1.验证toekn
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);

        // 2.解析Json数据
        GroupMasterDTO groupMasterDTO = StockOutFrom2GroupMasterDTOConverter.convert(stockOutForm);


        //3.调整寝室商品数量
        BigDecimal groupProductAmount = new BigDecimal(BigInteger.ZERO);
        for (GroupProduct groupProduct : groupMasterDTO.getGroupProductList()){
            GroupProduct product = groupProductService.findBySchoolNoAndGroupNoAndProductId
                    (sellerInfo.getSchoolNo(),groupProduct.getGroupNo(),groupProduct.getProductId());
            if (product==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            groupProductAmount = product.getProductPrice()
                    .multiply(new BigDecimal(groupProduct.getProductQuantity()))
                    .add(groupProductAmount);
            if (product.getProductStock().equals(groupProduct.getProductQuantity()))
            {
               // groupProductService.removeAll(product);
                product.setProductStock(0);
            }
            else {
               // product.setProductQuantity(product.getProductQuantity() - groupProduct.getProductQuantity());
                product.setProductStock(product.getProductStock() - groupProduct.getProductQuantity());
                if (product.getProductStock()<0){
                    throw new SellException(ResultEnum.GROUP_PRODUCT_STOCKOUT_ERROR);
                }
            }
            product.setProductStockout(product.getProductStockout() + groupProduct.getProductQuantity());
            groupProductService.save(product);
        }
        String groupNo = groupMasterDTO.getGroupProductList().get(0).getGroupNo();
        GroupMasterDTO group = groupMasterService.findByGroupNoAndSchoolNo
                (groupNo,sellerInfo.getSchoolNo());
        if (group==null){
            log.error("【回收商品】,寝室为空");
            throw new SellException(ResultEnum.GROUP_PRODUCT_STOCKOUT_ERROR);
        }
        group.setGroupAmount(group.getGroupAmount().subtract(groupProductAmount));
        if (group.getGroupAmount().compareTo(new BigDecimal(BigInteger.ZERO))<0)
        {
            throw new SellException(ResultEnum.GROUP_PRODUCT_STOCKOUT_ERROR);
        }
        GroupMaster groupMaster = new GroupMaster();
        BeanUtils.copyProperties(group,groupMaster);
        groupMasterService.save(groupMaster);

        //4.回收库存
        List<CartDTO> cartDTOList = groupMasterDTO.getGroupProductList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());

        productService.increaseStock(cartDTOList,sellerInfo.getSchoolNo());


        return ResultVOUtil.success();
    }

}
