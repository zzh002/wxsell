package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.BoxApply;
import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.enums.DeleteStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.BoxApplyForm;
import com.hnust.wxsell.repository.BoxApplyRepository;
import com.hnust.wxsell.repository.GroupMasterRepository;
import com.hnust.wxsell.service.BoxApplyService;
import com.hnust.wxsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author ZZH
 * @date 2018/4/2 0002 13:10
 **/
@Service
@Slf4j
public class BoxApplyServiceImpl implements BoxApplyService {

    @Autowired
    private BoxApplyRepository boxApplyRepository;

    @Autowired
    private GroupMasterRepository groupMasterRepository;

    @Override
    public BoxApply create(BoxApplyForm boxApplyForm) {
        BoxApply boxApply = new BoxApply();
        BeanUtils.copyProperties(boxApplyForm,boxApply);
        boxApply.setId(KeyUtil.genUniqueKey());

        return boxApplyRepository.save(boxApply);
    }

    @Override
    public BoxApply delete(String id) {
        //1.判断盒子申请订单是否存在
        BoxApply boxApply = boxApplyRepository.findOne(id);
        if (boxApply==null){
            log.error("【删除盒子申请表单】,表单不存在");
            throw new SellException(ResultEnum.BOX_APPLY_NOT_EXIST);
        }
        //2.判断盒子申请订单状态是否正确
        if (!boxApply.getDeleteStatus().equals(DeleteStatusEnum.NOT_DELETED.getCode())){
            log.error("【删除盒子申请表单】,表单状态不正确");
            throw new SellException(ResultEnum.DELETE_STATUS_ERROR);
        }
        //3.修改删除状态
        boxApply.setDeleteStatus(DeleteStatusEnum.DELETED.getCode());
        BoxApply result = boxApplyRepository.save(boxApply);
        if (result==null){
            log.error("【删除盒子申请表单】,表单更新失败");
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return result;
    }

    @Override
    public BoxApply findOne(String id) {
        BoxApply result = boxApplyRepository.findOne(id);
        if (result==null){
            log.error("【查询单个盒子申请表单】,表单不存在");
            throw new SellException(ResultEnum.BOX_APPLY_NOT_EXIST);
        }
        return result;
    }

    @Override
    public Page<BoxApply> findExistAllBySchoolNo(Pageable pageable,String schoolNo) {
        Page<BoxApply> boxApplyPage = boxApplyRepository.findBySchoolNoAndDeleteStatus(schoolNo,
                DeleteStatusEnum.NOT_DELETED.getCode(),pageable);

        return boxApplyPage;
    }

    @Override
    public Page<BoxApply> findExistAllBySchoolNoAndDistrict(Pageable pageable, String schoolNo, String groupDistrict) {
        Page<BoxApply> boxApplyPage = boxApplyRepository.findBySchoolNoAndDeleteStatusAndGroupDistrict(schoolNo,
                DeleteStatusEnum.NOT_DELETED.getCode(),groupDistrict,pageable);

        return boxApplyPage;
    }

    @Override
    public GroupMaster save(String id) {

        BoxApply boxApply = boxApplyRepository.findOne(id);
        GroupMaster groupMaster = new GroupMaster();
        groupMaster.setGroupId(KeyUtil.genUniqueKey());
        groupMaster.setGroupNo(boxApply.getGroupNo());
        groupMaster.setSchoolNo(boxApply.getSchoolNo());
        groupMaster.setGroupDistrict(boxApply.getGroupDistrict());
        groupMaster.setUserName(boxApply.getUserName());
        groupMaster.setUserPhone(boxApply.getUserPhone());

        GroupMaster result = groupMasterRepository.save(groupMaster);
        if (result==null){
            log.error("【添加寝室信息】，添加失败");
            throw new SellException(ResultEnum.BOX_APPLY_SAVE_ERROR);
        }
        return result;
    }
}
