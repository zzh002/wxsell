package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/2  11:23
 **/
public interface BannerRepository extends JpaRepository<Banner , Integer> {
    Banner findByBannerId(Integer bannerid);
    List<Banner> findByBannerName(String bannerName);
}
