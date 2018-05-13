package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.Banner;
import com.hnust.wxsell.dataobject.BannerItem;
import com.hnust.wxsell.dto.BannerDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.BannerItemRepository;
import com.hnust.wxsell.repository.BannerRepository;
import com.hnust.wxsell.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:32
 **/
@Service
@Slf4j
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerItemRepository bannerItemRepository;

    @Override
    public BannerDTO findByBannerId(Integer bannerId) {
        //1.查轮播管理表
        BannerDTO bannerDTO = new BannerDTO();
        Banner banner = bannerRepository.findByBannerId(bannerId);
        if (banner==null){
            log.error("【轮播图】 轮播类型不存在. bannerId={}", bannerId);
            throw new SellException(ResultEnum.BANNER_NOT_EXIST);
        }

        //2.取数据
        List<BannerItem> bannerItemList = bannerItemRepository.findByBannerId(bannerId);

        BeanUtils.copyProperties(banner,bannerDTO);
        bannerDTO.setBannerItemList(bannerItemList);
        return bannerDTO;
    }
}
