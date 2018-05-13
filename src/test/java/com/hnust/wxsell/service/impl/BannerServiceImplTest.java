package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dto.BannerDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:40
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class BannerServiceImplTest {

    @Autowired
    private BannerServiceImpl bannerService;

    @Test
    public void findByBannerId() {
        BannerDTO bannerDTO = bannerService.findByBannerId(1);
        Assert.assertNotNull(bannerDTO);
    }
}