package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dataobject.ReplenishMaster;
import com.hnust.wxsell.dto.ReplenishDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:46
 **/
public interface ReplenishService {

    /** 创建订单. */
    ReplenishDTO create(ReplenishDTO replenishDTO);

    /** 查询单个订单. */
    ReplenishDTO findOne(String replenishId);

    /** 查询订单列表，买家端. */
    Page<ReplenishDTO> findList(String openId, Pageable pageable);

    /** 取消订单. */
    ReplenishDTO cancel(ReplenishDTO replenishDTO);

    /** 生成配送订单. */
    ReplenishDTO finish(ReplenishDTO replenishDTO);

    /** 查询所有订单列表，卖家端. */
    Page<ReplenishDTO> findListBySchoolNo(String schoolNo , Pageable pageable);

    /** 查询所有新订单列表. */
    Page<ReplenishDTO> findNewReplenish(String schoolNo ,Pageable pageable);

    /** 保存补货单信息*/
    ReplenishDTO save(ReplenishDTO replenishDTO);

    /** 删除补货订单详情*/
    void delete(ReplenishDetail replenishDetail);

    ReplenishMaster save(ReplenishMaster replenishMaster);

    ReplenishDetail save(ReplenishDetail replenishDetail);
}
