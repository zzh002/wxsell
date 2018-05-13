package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 16:14
 **/
public interface GroupMasterService {

    //查询校区所有寝室列表
    List<GroupMaster> findBySchoolNo(String schoolNo);


    //查询校区某寝室区所有寝室列表
    Page<GroupMaster> findByGroupDistrictAndSchoolNo(String groupDistrict,String schoolNo,
                                                     Pageable pageable);

    //模糊查询寝室
    List<GroupMaster> findByGroupNo(String schoolNo , String groupNo);

    GroupMaster findOne(String id);

    GroupMaster save(GroupMaster groupMaster);

    // 卖家端查询寝室列表
    Page<GroupMasterDTO> findList( String schoolNo ,Pageable pageable);

    /** 卖家端查询寝室商品列表. */
    GroupMasterDTO  findByGroupNoAndSchoolNo(String groupNo,String schoolNo);

    /** 用户消费，增加寝室消费金额 */
    void consumePaid(OrderDTO orderDTO);

    /** 订单退款，减少寝室消费金额 */
    void consumeRefund(OrderDTO orderDTO);


}
