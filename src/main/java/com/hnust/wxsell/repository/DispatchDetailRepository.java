package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.DispatchDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/9 0009 12:57
 **/
public interface DispatchDetailRepository extends JpaRepository<DispatchDetail, String> {

    List<DispatchDetail> findByDispatchId(String dispatchId);

    void removeAllByDispatchId(String dispatchId);

    DispatchDetail findByDispatchIdAndProductId(String dispatchId , String productId);
}
