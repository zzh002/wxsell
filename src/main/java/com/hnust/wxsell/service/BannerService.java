package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.BannerDTO;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:23
 **/
public interface BannerService {
    BannerDTO findByBannerId(Integer bannerId);
}
