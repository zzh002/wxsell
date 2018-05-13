package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.BannerItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:25
 **/
public interface BannerItemRepository extends JpaRepository<BannerItem, Integer> {
    List<BannerItem> findByBannerId(Integer bannerId);
}
