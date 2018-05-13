package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.converter.GroupMaster2GroupMasterDTOConverter;
import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.OrderDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.GroupMasterRepository;
import com.hnust.wxsell.repository.GroupProductRepository;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.utils.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/7 0007 0:06
 **/
@Service
public class GroupMasterServiceImpl implements GroupMasterService {
    @Autowired
    private GroupMasterRepository groupMasterRepository;
    @Autowired
    private GroupProductRepository groupProductRepository;

    @Override
    public List<GroupMaster> findBySchoolNo(String schoolNo) {
        return groupMasterRepository.findBySchoolNo(schoolNo);
    }

    @Override
    public Page<GroupMaster> findByGroupDistrictAndSchoolNo(String groupDistrict, String schoolNo
           , Pageable pageable) {
        Page<GroupMaster> groupMasterPage = groupMasterRepository.findByGroupDistrictAndSchoolNo(groupDistrict,schoolNo,pageable);
        return new PageImpl<>(groupMasterPage.getContent(), pageable, groupMasterPage.getTotalElements());
    }

    @Override
    public List<GroupMaster> findByGroupNo(String schoolNo, String groupNo) {
        return groupMasterRepository.findBySchoolNoAndGroupNoContaining(schoolNo,groupNo);
    }

    @Override
    public GroupMaster findOne(String id) {
        return groupMasterRepository.findOne(id);
    }

    @Override
    public GroupMaster save(GroupMaster groupMaster) {
        return groupMasterRepository.save(groupMaster);
    }

    @Override
    public Page<GroupMasterDTO> findList( String schoolNo ,Pageable pageable) {
        Page<GroupMaster> groupMasterPage = groupMasterRepository.findBySchoolNo(schoolNo,pageable);

        List<GroupMasterDTO> groupMasterDTOList = GroupMaster2GroupMasterDTOConverter.convert(groupMasterPage.getContent());

        return new PageImpl<>(groupMasterDTOList, pageable, groupMasterPage.getTotalElements());
    }

    @Override
    public GroupMasterDTO findByGroupNoAndSchoolNo(String groupNo,String schoolNo) {
        GroupMaster groupMaster = groupMasterRepository.findByGroupNoAndSchoolNo(groupNo,schoolNo);
        if (groupMaster == null) {
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }

        List<GroupProduct> groupProductList = groupProductRepository.findBySchoolNoAndGroupNo(schoolNo,groupNo);
        // 按productStock降序、productPrice降序、productId升序排序
        String [] sortNameArr = {"productStock","productPrice","productId"};
        boolean [] isAscArr = {false,false,true};
        ListUtils.sort(groupProductList,sortNameArr, isAscArr);

        GroupMasterDTO groupMasterDTO = new GroupMasterDTO();
        BeanUtils.copyProperties(groupMaster, groupMasterDTO);
        groupMasterDTO.setGroupProductList(groupProductList);

        return groupMasterDTO;
    }

    @Override
    public void consumePaid(OrderDTO orderDTO) {
        GroupMaster groupMaster = groupMasterRepository.
                findByGroupNoAndSchoolNo(orderDTO.getGroupNo(),orderDTO.getSchoolNo());
        //增加寝室消费金额，减少寝室剩余商品金额
        groupMaster.setGroupConsume(groupMaster.getGroupConsume().add(orderDTO.getOrderAmount()));
        groupMaster.setGroupAmount(groupMaster.getGroupAmount().subtract(orderDTO.getOrderAmount()));

        groupMasterRepository.save(groupMaster);
    }

    @Override
    public void consumeRefund(OrderDTO orderDTO) {
        GroupMaster groupMaster = groupMasterRepository.
                findByGroupNoAndSchoolNo(orderDTO.getGroupNo(),orderDTO.getSchoolNo());

        groupMaster.setGroupConsume(groupMaster.getGroupConsume().subtract(orderDTO.getOrderAmount()));
        groupMaster.setGroupAmount(groupMaster.getGroupAmount().add(orderDTO.getOrderAmount()));

        groupMasterRepository.save(groupMaster);
    }
}
