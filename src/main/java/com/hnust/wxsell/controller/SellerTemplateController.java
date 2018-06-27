package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.converter.TemplateForm2TemplateDTOConverter;
import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.TemplateDetail;
import com.hnust.wxsell.dataobject.TemplateMaster;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.dto.TemplateDTO;
import com.hnust.wxsell.enums.DeleteStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;

import com.hnust.wxsell.form.TemplateForm;

import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.TemplateService;
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
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/10 0010 14:54
 **/
@RestController
@RequestMapping("/seller/template")
@Slf4j
public class SellerTemplateController {
    @Autowired
    TemplateService templateService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private ProductService productService;



    /**
     * 模板列表
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_NOT_EXIST);
        }
        List<TemplateDTO> templateDTOList = templateService.findList(sellerInfo.getSchoolNo());

        return ResultVOUtil.success(templateDTOList);
    }

    /**
     * 创建补货单
     * @param token
     * @param groupNo
     * @param templateId
     * @return
     */
    @GetMapping("/create_replenish")
    public ResultVO create(@RequestParam("token") String token,
                             @RequestParam("groupNo") String groupNo,
                             @RequestParam("templateId") String templateId){

        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_NOT_EXIST);
        }

        try {
            //String groupNo = templateService.findOne(templateId).getTemplateName();
            templateService.createReplenishByTemplate(sellerInfo.getSchoolNo(),groupNo,templateId);
        }catch (SellException e){
            log.error("【买极端模板补货】发生异常{}", e);
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return  ResultVOUtil.success();
    }

    /**
     * 模板详情
     * @param templateId
     * @param token
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("templateId") String templateId,
                           @RequestParam("token") String token){
        TemplateDTO templateDTO = new TemplateDTO();
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        try {
            templateDTO = templateService.findOne(templateId);
        }catch (SellException e){
            log.error("【卖家端查询订单详情】发生异常{}", e);
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success(templateDTO);
    }

    /**
     * 删除模板
     * @param templateId
     * @return
     */
    @GetMapping("/delete")
    public ResultVO delete(@RequestParam("templateId") String templateId){

        TemplateDTO templateDTO = new TemplateDTO();
       // String redirectUrl = projectUrlConfig.getSell()+"/sell/seller/template/list";
        try {
            templateDTO = templateService.delete(templateId);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return  ResultVOUtil.success();
    }

    /**
     * 修改模板名
     * @param templateId
     * @param token
     * @param templateName
     * @return
     */
    @GetMapping("/save")
    public ResultVO save(@RequestParam("templateId") String templateId,
                           @RequestParam("token") String token,
                         @RequestParam("templateName") String templateName){
        TemplateDTO templateDTO = new TemplateDTO();
        TemplateMaster templateMaster = new TemplateMaster();
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        try {
            templateDTO = templateService.findOne(templateId);
            BeanUtils.copyProperties(templateDTO,templateMaster);
            templateMaster.setTemplateName(templateName);
            templateService.update(templateMaster);
        }catch (SellException e){
            log.error("【卖家端查询订单详情】发生异常{}", e);
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
        return ResultVOUtil.success();
    }

    /**
     * 直接创建模板
     * @param templateForm
     * @param token
     * @param bindingResult
     * @param response
     * @return
     */
    @PostMapping("/create")
    public ResultVO create(@Valid TemplateForm templateForm,
                           @RequestParam("token") String token,
                           BindingResult bindingResult,
                           HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", templateForm);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        // 1.验证token
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        // 2.解析Json数据
        TemplateDTO templateDTO = TemplateForm2TemplateDTOConverter.convert(templateForm);
        //3.判断是否存在已删除模板
        TemplateDTO result = templateService.findByTemplateName(sellerInfo.getSchoolNo(),templateForm.getTemplateName());
        if (result != null){
            if (result.getDeleteStatus().equals(DeleteStatusEnum.DELETED.getCode())){
                try {
                  /*  for (TemplateDetail detail : details){
                        templateDetailRepository.delete(detail);
                    }*/
                   templateService.delete(result);
                } catch (SellException e){
                    throw new SellException(ResultEnum.TEMPLATE_UPDATE_FAIL.getCode(),ResultEnum.TEMPLATE_UPDATE_FAIL.getMessage());
                }
            }else {
                throw new SellException(ResultEnum.TEMPLATE_EXIST.getCode(),ResultEnum.TEMPLATE_EXIST.getMessage());
            }

        }
        //4.保存模板信息
        TemplateMaster templateMaster = new TemplateMaster();
        templateMaster.setTemplateId(KeyUtil.genUniqueKey());
        templateMaster.setSchoolNo(sellerInfo.getSchoolNo());
        templateMaster.setTemplateName(templateForm.getTemplateName());
        templateService.update(templateMaster);
        List<TemplateDetail> templateDetailList = templateDTO.getTemplateDetailList();
        for (TemplateDetail templateDetail : templateDetailList){
            ProductDTO productDTO = productService.findOne(templateDetail.getProductId());
            if (productDTO==null){
                return ResultVOUtil.error(ResultEnum.PRODUCT_NOT_EXIST.getCode(),
                        ResultEnum.PRODUCT_NOT_EXIST.getMessage());
            }
            templateDetail.setDetailId(KeyUtil.genUniqueKey());
            templateDetail.setTemplateId(templateMaster.getTemplateId());
            templateDetail.setProductName(productDTO.getProductName());
            templateService.save(templateDetail);
        }
        return ResultVOUtil.success();
    }
}
