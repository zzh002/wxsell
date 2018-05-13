package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.Banner;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/2  11:27
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BannerRepositoryTest {

    @Autowired
    private BannerRepository repository;

    @Test
    public void findByBannerId() {
        Banner banner = repository.findByBannerId(1);
        Assert.assertNotNull(banner);
    }

    @Test
    public void save(){
        Banner banner = new Banner();
        banner.setBannerId(1);
        banner.setBannerName("轮播图");
        banner.setBannerDescription("轮播图");
        Banner result = repository.save(banner);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBannerName(){
        List<Banner> bannerList = repository.findByBannerName("轮播图");
        for (Banner banner:bannerList) {
            log.info("bannerId:{},bannerDescription:{}", banner.getBannerId(), banner.getBannerDescription());
        }
        Assert.assertNotNull(bannerList);
    }
}