package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.DispatchDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZZH
 * @date 2018/4/9 0009 13:13
 **/
public interface DispatchService {

    /* 查询寝室配送的商品. */
    DispatchDTO findOne(String schoolNo , String groupNo);

    /* 增加补货商品数量. */
    ReplenishDTO add(ReplenishDTO replenishDTO);

    /* 取消补货定单  同时清空寝室购物车. */
    void cancel(DispatchDTO dispatchDTO);

    /* 完成寝室补货, 商品入库*/
    void finish(String schoolNo , String groupNo);

    /** 查询补货列表. */
    Page<DispatchDTO> findList(String schoolNo , Pageable pageable);
}
