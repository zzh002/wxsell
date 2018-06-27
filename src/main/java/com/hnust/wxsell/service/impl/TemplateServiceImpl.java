package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.converter.TemplateMaster2TemplateDTOConverter;
import com.hnust.wxsell.dataobject.DispatchDetail;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dataobject.TemplateDetail;
import com.hnust.wxsell.dataobject.TemplateMaster;
import com.hnust.wxsell.dto.*;
import com.hnust.wxsell.enums.DeleteStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.TemplateDetailRepository;
import com.hnust.wxsell.repository.TemplateMasterRepository;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.ReplenishService;
import com.hnust.wxsell.service.TemplateService;
import com.hnust.wxsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/10 0010 14:44
 **/
@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    ProductService productService;

    @Autowired
    TemplateDetailRepository templateDetailRepository;

    @Autowired
    TemplateMasterRepository templateMasterRepository;

    @Autowired
    GroupMasterService groupMasterService;

    @Autowired
    ReplenishService replenishService;

    @Override
    public TemplateDTO save(DispatchDTO dispatchDTO,String templateName) {
        String templateId = KeyUtil.genUniqueKey();

        //写入模板
        TemplateMaster templateMaster = new TemplateMaster();
        templateMaster.setTemplateId(templateId);
        templateMaster.setTemplateName(templateName);
        templateMaster.setSchoolNo(dispatchDTO.getSchoolNo());

        TemplateMaster result = templateMasterRepository.findBySchoolNoAndTemplateName(dispatchDTO.getSchoolNo(),templateName);
        if (result != null){
            if (result.getDeleteStatus().equals(DeleteStatusEnum.DELETED.getCode())){
                List<TemplateDetail> details = templateDetailRepository.findByTemplateId(result.getTemplateId());
                try {
                  /*  for (TemplateDetail detail : details){
                        templateDetailRepository.delete(detail);
                    }*/
                    templateDetailRepository.delete(details);
                    templateMasterRepository.delete(result);
                } catch (SellException e){
                    throw new SellException(ResultEnum.TEMPLATE_UPDATE_FAIL.getCode(),ResultEnum.TEMPLATE_UPDATE_FAIL.getMessage());
                }
            }else {
                throw new SellException(ResultEnum.TEMPLATE_EXIST.getCode(),ResultEnum.TEMPLATE_EXIST.getMessage());
            }

        }
        templateMasterRepository.save(templateMaster);
        //查询配送单（数量）
        for (DispatchDetail dispatchDetail : dispatchDTO.getDispatchDetailList()) {

            //查找商品
            ProductDTO productDTO = productService.findOne(dispatchDetail.getProductId());
            if (productDTO == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            TemplateDetail templateDetail = new TemplateDetail();
            //模板详情入库
            BeanUtils.copyProperties(dispatchDetail, templateDetail);
            templateDetail.setDetailId(KeyUtil.genUniqueKey());
            templateDetail.setTemplateId(templateId);
            templateDetailRepository.save(templateDetail);
        }

        return TemplateMaster2TemplateDTOConverter.convert(templateMaster);
    }

    @Override
    public TemplateDTO delete(String templateId) {
        TemplateMaster templateMaster = templateMasterRepository.findOne(templateId);
        //判断订单是否存在
        if(templateMaster == null){
            throw new SellException(ResultEnum.TEMPLATE_NOT_EXIST);
        }
        //判断订单状态
        if (!templateMaster.getDeleteStatus().equals(DeleteStatusEnum.NOT_DELETED.getCode())){
            throw new SellException(ResultEnum.DELETE_STATUS_ERROR);
        }

        //修改订单状态
        templateMaster.setDeleteStatus(DeleteStatusEnum.DELETED.getCode());
        TemplateMaster updateResult = templateMasterRepository.save(templateMaster);
        if (updateResult == null) {
            throw new SellException(ResultEnum.TEMPLATE_UPDATE_FAIL);
        }

        return TemplateMaster2TemplateDTOConverter.convert(updateResult);
    }

    @Override
    public List<TemplateDTO> findList(String schoolNo) {
        List<TemplateMaster> templateMasterList = templateMasterRepository.findByDeleteStatusAndSchoolNo
                (DeleteStatusEnum.NOT_DELETED.getCode(),schoolNo);

        return TemplateMaster2TemplateDTOConverter.convert(templateMasterList);
    }

    @Override
    public TemplateDTO findOne(String templateId) {
        TemplateMaster templateMaster = templateMasterRepository.findOne(templateId);

        if(templateMaster == null){
            throw new SellException(ResultEnum.TEMPLATE_NOT_EXIST);
        }

        List<TemplateDetail> templateDetailList = templateDetailRepository.findByTemplateId(templateId);

        TemplateDTO templateDTO = TemplateMaster2TemplateDTOConverter.convert(templateMaster);

        templateDTO.setTemplateDetailList(templateDetailList);

        return templateDTO;
    }

    @Override
    public TemplateDTO findByTemplateName(String schoolNo, String templateName) {
        TemplateMaster templateMaster = templateMasterRepository.
                findBySchoolNoAndTemplateName(schoolNo, templateName);
        List<TemplateDetail> templateDetailList = templateDetailRepository.
                findByTemplateId(templateMaster.getTemplateId());
        TemplateDTO templateDTO = new TemplateDTO();
        BeanUtils.copyProperties(templateMaster,templateDTO);
        templateDTO.setTemplateDetailList(templateDetailList);
        return templateDTO;
    }

    @Override
    public ReplenishDTO createReplenishByTemplate(String schoolNo, String groupNo, String templateId) {
        GroupMasterDTO groupMasterDTO = groupMasterService.findByGroupNoAndSchoolNo
                (groupNo,schoolNo);

        if (groupMasterDTO == null){
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }

        TemplateMaster templateMaster = templateMasterRepository.findOne(templateId);

        if (templateMaster == null){
            throw new SellException(ResultEnum.TEMPLATE_NOT_EXIST);
        }

        List<TemplateDetail> templateDetailList = templateDetailRepository.findByTemplateId(templateId);

        List<ReplenishDetail> replenishDetailList = templateDetailList.stream().map(e ->
                new ReplenishDetail(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());

        ReplenishDTO replenishDTO = new ReplenishDTO();

        replenishDTO.setSchoolNo(groupMasterDTO.getSchoolNo());
        replenishDTO.setGroupNo(groupMasterDTO.getGroupNo());
        replenishDTO.setUserName(groupMasterDTO.getUserName());
        replenishDTO.setUserPhone(groupMasterDTO.getUserPhone());
        replenishDTO.setOpenId("seller");
        replenishDTO.setReplenishDetailList(replenishDetailList);

        return replenishService.create(replenishDTO);
    }

    @Override
    public TemplateMaster update(TemplateMaster templateMaster) {
        return templateMasterRepository.save(templateMaster);
    }

    @Override
    public TemplateDetail save(TemplateDetail templateDetail) {
        return templateDetailRepository.save(templateDetail);
    }

    @Override
    public void delete(TemplateDTO templateDTO) {
        TemplateMaster templateMaster = new TemplateMaster();
        BeanUtils.copyProperties(templateDTO,templateMaster);
        templateDetailRepository.delete(templateDTO.getTemplateDetailList());
        templateMasterRepository.delete(templateMaster);
    }
}
