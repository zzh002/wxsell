package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.converter.DispatchMaster2DispatchDTOConverter;
import com.hnust.wxsell.dataobject.DispatchDetail;
import com.hnust.wxsell.dataobject.DispatchMaster;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dto.*;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.DispatchDetailRepository;
import com.hnust.wxsell.repository.DispatchMasterRepository;
import com.hnust.wxsell.service.DispatchService;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.service.GroupProductService;
import com.hnust.wxsell.service.ProductService;
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
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/10 0010 13:59
 **/
@Service
@Slf4j
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private DispatchDetailRepository dispatchDetailRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DispatchMasterRepository dispatchMasterRepository;

    @Autowired
    private GroupProductService groupProductService;

    @Autowired
    private GroupMasterService groupMasterService;
    @Override
    public DispatchDTO findOne(String schoolNo, String groupNo) {
        DispatchMaster dispatchMaster = dispatchMasterRepository.findBySchoolNoAndGroupNo(schoolNo,groupNo);
        if(dispatchMaster == null){
            throw new SellException(ResultEnum.REPLENISH_NOT_EXIST);
        }

        List<DispatchDetail> dispatchDetailList = dispatchDetailRepository.
                findByDispatchId(dispatchMaster.getDispatchId());

        DispatchDTO dispatchDTO = new DispatchDTO();
        BeanUtils.copyProperties(dispatchMaster, dispatchDTO);
        dispatchDTO.setDispatchDetailList(dispatchDetailList);

        return dispatchDTO;
    }

    @Override
    @Transactional
    public ReplenishDTO add(ReplenishDTO replenishDTO) {
        BigDecimal dispatchAmount = new BigDecimal(BigInteger.ZERO);

        //验证补货的寝室是否存在
        GroupMasterDTO groupMasterDTO = groupMasterService.findByGroupNoAndSchoolNo
                (replenishDTO.getGroupNo(),replenishDTO.getSchoolNo());
        if (groupMasterDTO == null){
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }

        DispatchMaster dispatchMaster = dispatchMasterRepository.findBySchoolNoAndGroupNo
                (replenishDTO.getSchoolNo(),replenishDTO.getGroupNo());

        //判断配送订单是否为空
        if(dispatchMaster == null){
            dispatchMaster = new DispatchMaster();
            dispatchMaster.setGroupNo(replenishDTO.getGroupNo());
            dispatchMaster.setSchoolNo(replenishDTO.getSchoolNo());
            dispatchMaster.setDispatchId(KeyUtil.genUniqueKey());
            dispatchMaster.setDispatchKind(0);
            dispatchMaster.setDispatchAmount(new BigDecimal(BigInteger.ZERO));
        }

        dispatchMasterRepository.save(dispatchMaster);
        //遍历补货的订单
        for (ReplenishDetail replenishDetail : replenishDTO.getReplenishDetailList()){
            //查找商品
            ProductDTO productDTO = productService.findOne(replenishDetail.getProductId());
            if (productDTO == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            if (replenishDetail.getProductQuantity() > 0){

            //2. 计算订单总价
            dispatchAmount = productDTO.getProductPrice()
                    .multiply(new BigDecimal(replenishDetail.getProductQuantity()))
                    .add(dispatchAmount);

            //配送单详情如库

            DispatchDetail foundResult = dispatchDetailRepository.
                    findByDispatchIdAndProductId(dispatchMaster.getDispatchId(),replenishDetail.getProductId());
            if (foundResult == null){
                foundResult = new DispatchDetail();
                BeanUtils.copyProperties(productDTO,foundResult);
                foundResult.setDispatchId(dispatchMaster.getDispatchId());
                foundResult.setId(KeyUtil.genUniqueKey());
                foundResult.setProductQuantity(0);
            }
            foundResult.setProductQuantity(foundResult.getProductQuantity()+replenishDetail.getProductQuantity());

            dispatchDetailRepository.save(foundResult);

            }
        }


        //写入配送订单数据库
        dispatchAmount = dispatchMaster.getDispatchAmount().add(dispatchAmount);
        dispatchMaster.setDispatchAmount(dispatchAmount);
        Integer kind = dispatchDetailRepository.
                findByDispatchId(dispatchMaster.getDispatchId()).size();
        dispatchMaster.setDispatchKind(kind);
        dispatchMasterRepository.save(dispatchMaster);
        //4. 扣库存
        List<CartDTO> cartDTOList = replenishDTO.getReplenishDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList,replenishDTO.getSchoolNo());

        return replenishDTO;
    }

    @Override
    @Transactional
    public void cancel(DispatchDTO dispatchDTO) {
        //返回库存
        if (CollectionUtils.isEmpty(dispatchDTO.getDispatchDetailList())) {
            log.error("【取消配送】没有补货商品, dispatchDTO={}", dispatchDTO);
            throw new SellException(ResultEnum.DISPATCH_NOT_EXIST);
        }

        //补货种类和金额清零
        DispatchMaster dispatchMaster = dispatchMasterRepository.
                findBySchoolNoAndGroupNo(dispatchDTO.getSchoolNo(),dispatchDTO.getGroupNo());
        if (dispatchMaster == null){
            throw new SellException(ResultEnum.DISPATCH_NOT_EXIST);
        }

        dispatchMaster.setDispatchAmount(BigDecimal.ZERO);
        dispatchMaster.setDispatchKind(0);
        dispatchMasterRepository.save(dispatchMaster);

        List<CartDTO> cartDTOList = dispatchDTO.getDispatchDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList,dispatchDTO.getSchoolNo());

        dispatchDetailRepository.removeAllByDispatchId(dispatchDTO.getDispatchId());
    }

    @Override
    @Transactional
    public void finish(String schoolNo, String groupNo) {

        DispatchDTO dispatchDTO = new DispatchDTO();

        //查询dispatchMaster表
        DispatchMaster dispatchMaster = dispatchMasterRepository.
                findBySchoolNoAndGroupNo(schoolNo,groupNo);
        if (dispatchMaster == null){
            throw new SellException(ResultEnum.DISPATCH_NOT_EXIST);
        }

        //查询补货详情
        List<DispatchDetail> dispatchDetailList = dispatchDetailRepository.
                findByDispatchId(dispatchMaster.getDispatchId());

        if (CollectionUtils.isEmpty(dispatchDetailList)){
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        BeanUtils.copyProperties(dispatchMaster, dispatchDTO);
        dispatchDTO.setDispatchDetailList(dispatchDetailList);

        //寝室商品补货
        groupProductService.replenish(dispatchDTO);

        //清空补货车
        dispatchDetailRepository.removeAllByDispatchId(dispatchMaster.getDispatchId());

        //补货车统计数据清零
        dispatchMaster.setDispatchKind(0);
        dispatchMaster.setDispatchAmount(new BigDecimal(0));
        dispatchMasterRepository.save(dispatchMaster);
    }

    @Override
    public Page<DispatchDTO> findList(String schoolNo, Pageable pageable) {
        //查找金额大于0的配送单列表
        Page<DispatchMaster> dispatchMasterPage = dispatchMasterRepository.findBySchoolNoAndDispatchAmountGreaterThan
                ( schoolNo,new BigDecimal(BigInteger.ZERO),pageable);

        List<DispatchDTO> dispatchDTOList = DispatchMaster2DispatchDTOConverter.convert(dispatchMasterPage.getContent());

        for (DispatchDTO dispatchDTO : dispatchDTOList){
            List<DispatchDetail> dispatchDetailList = dispatchDetailRepository.findByDispatchId(dispatchDTO.getDispatchId());
            dispatchDTO.setDispatchDetailList(dispatchDetailList);
        }
        return new PageImpl<DispatchDTO>(dispatchDTOList, pageable, dispatchMasterPage.getTotalElements());
    }
}
