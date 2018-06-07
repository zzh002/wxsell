package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.TemplateMaster;
import com.hnust.wxsell.dto.TemplateDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.TemplateService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private ProjectUrlConfig projectUrlConfig;

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
}
