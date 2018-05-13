package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.converter.ReplenishMaster2ReplenishDTOConverter;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dataobject.ReplenishMaster;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.enums.OrderStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.ReplenishDetailRepository;
import com.hnust.wxsell.repository.ReplenishMasterRepository;
import com.hnust.wxsell.service.DispatchService;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.service.ReplenishService;
import com.hnust.wxsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/8 0008 21:13
 **/
@Service
@Slf4j
public class ReplenishServiceImpl implements ReplenishService {
    @Autowired
    ReplenishDetailRepository replenishDetailRepository;

    @Autowired
    ReplenishMasterRepository replenishMasterRepository;

    @Autowired
    ProductService productService;

    @Autowired
    private DispatchService dispatchService;
    @Autowired
    GroupMasterService groupMasterService;

    @Override
    @Transactional
    public ReplenishDTO create(ReplenishDTO replenishDTO) {
        if (CollectionUtils.isEmpty(replenishDTO.getReplenishDetailList())){
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        String replenishId = KeyUtil.genUniqueKey();
        BigDecimal replenishAmount = new BigDecimal(BigInteger.ZERO);

        //1. 查询商品（数量, 价格）
        for (ReplenishDetail replenishDetail : replenishDTO.getReplenishDetailList()) {
            ProductDTO productDTO =  productService.findOne(replenishDetail.getProductId());
            if (productDTO == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2. 计算订单总价
            replenishAmount = productDTO.getProductPrice()
                    .multiply(new BigDecimal(replenishDetail.getProductQuantity()))
                    .add(replenishAmount);

            //订单详情入库
            replenishDetail.setReplenishId(replenishId);
            BeanUtils.copyProperties(productDTO, replenishDetail);
            replenishDetail.setDetailId(KeyUtil.genUniqueKey());

            replenishDetailRepository.save(replenishDetail);
        }


        //3. 写入订单数据库（orderMaster和orderDetail）
        ReplenishMaster replenishMaster = new ReplenishMaster();
        replenishDTO.setReplenishId(replenishId);
        BeanUtils.copyProperties(replenishDTO, replenishMaster);
        replenishMaster.setReplenishAmount(replenishAmount);
        replenishMaster.setReplenishStatus(OrderStatusEnum.NEW.getCode());
        replenishMasterRepository.save(replenishMaster);

        return replenishDTO;
    }

    @Override
    public ReplenishDTO findOne(String replenishId) {
        ReplenishMaster replenishMaster = replenishMasterRepository.findOne(replenishId);
        if (replenishMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<ReplenishDetail> replenishDetailList = replenishDetailRepository.findByReplenishId(replenishId);
        if (CollectionUtils.isEmpty(replenishDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        ReplenishDTO replenishDTO = new ReplenishDTO();
        BeanUtils.copyProperties(replenishMaster, replenishDTO);
        replenishDTO.setReplenishDetailList(replenishDetailList);

        return replenishDTO;
    }

    @Override
    public Page<ReplenishDTO> findList(String openId, Pageable pageable) {
        Page<ReplenishMaster> replenishMasterPage = replenishMasterRepository.findByOpenId(openId, pageable);

        List<ReplenishDTO> replenishDTOList = ReplenishMaster2ReplenishDTOConverter.convert(replenishMasterPage.getContent());

        return new PageImpl<ReplenishDTO>(replenishDTOList, pageable, replenishMasterPage.
                getTotalElements());
    }

    @Override
    @Transactional
    public ReplenishDTO cancel(ReplenishDTO replenishDTO) {
        ReplenishMaster replenishMaster = new ReplenishMaster();

        //判断订单状态
        if (!replenishDTO.getReplenishStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消补货订单】订单状态不正确, replenishId={}, replenishStatus={}", replenishDTO.getReplenishId(), replenishDTO.getReplenishStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        replenishDTO.setReplenishStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(replenishDTO, replenishMaster);
        ReplenishMaster updateResult = replenishMasterRepository.save(replenishMaster);
        if (updateResult == null) {
            log.error("【取消补货订单】更新失败, replenishMaster={}", replenishMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return replenishDTO;
    }

    @Override
    @Transactional
    public ReplenishDTO finish(ReplenishDTO replenishDTO) {
        //判断订单状态
        if (!replenishDTO.getReplenishStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结补货订单】订单状态不正确, replenishId={}, replenishStatus={}", replenishDTO.getReplenishId(), replenishDTO.getReplenishStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        replenishDTO.setReplenishStatus(OrderStatusEnum.FINISHED.getCode());
        ReplenishMaster replenishMaster = new ReplenishMaster();
        BeanUtils.copyProperties(replenishDTO, replenishMaster);
        ReplenishMaster updateResult = replenishMasterRepository.save(replenishMaster);
        if (updateResult == null) {
            log.error("【完结补货订单】更新失败, replenishMaster={}", replenishMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

       return dispatchService.add(replenishDTO);

    }

    @Override
    public Page<ReplenishDTO> findListBySchoolNo(String schoolNo, Pageable pageable) {
        Page<ReplenishMaster> replenishMasterPage = replenishMasterRepository.findBySchoolNo(schoolNo,pageable);

        List<ReplenishDTO> replenishDTOList = ReplenishMaster2ReplenishDTOConverter.convert(replenishMasterPage.getContent());
        for(ReplenishDTO replenishDTO : replenishDTOList){
            List<ReplenishDetail> replenishDetailList = replenishDetailRepository.findByReplenishId(replenishDTO.getReplenishId());
            replenishDTO.setReplenishDetailList(replenishDetailList);
        }

        return new PageImpl<>(replenishDTOList, pageable, replenishMasterPage.getTotalElements());
    }

    @Override
    public Page<ReplenishDTO> findNewReplenish(String schoolNo, Pageable pageable) {
        Page<ReplenishMaster> replenishMasterPage = replenishMasterRepository.
                findByReplenishStatusAndSchoolNo( OrderStatusEnum.NEW.getCode(),schoolNo,pageable);

        List<ReplenishDTO> replenishDTOList = ReplenishMaster2ReplenishDTOConverter.convert(replenishMasterPage.getContent());

        return new PageImpl<>(replenishDTOList, pageable, replenishMasterPage.getTotalElements());
    }

    @Override
    public ReplenishDTO save(ReplenishDTO replenishDTO) {
        ReplenishMaster replenishMaster = new ReplenishMaster();
        BeanUtils.copyProperties(replenishDTO,replenishMaster);
        replenishMasterRepository.save(replenishMaster);
        for (ReplenishDetail replenishDetail : replenishDTO.getReplenishDetailList()){
            if (replenishDetail!=null) {
                replenishDetailRepository.save(replenishDetail);
            }
        }
        return replenishDTO;
    }

    @Override
    public void delete(ReplenishDetail replenishDetail) {
        replenishDetailRepository.delete(replenishDetail.getDetailId());
    }

    @Override
    public ReplenishMaster save(ReplenishMaster replenishMaster) {
        return replenishMasterRepository.save(replenishMaster);
    }

    @Override
    public ReplenishDetail save(ReplenishDetail replenishDetail) {
        return replenishDetailRepository.save(replenishDetail);
    }
}
