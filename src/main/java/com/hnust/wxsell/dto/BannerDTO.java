package com.hnust.wxsell.dto;

import com.hnust.wxsell.dataobject.BannerItem;
import lombok.Data;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/2 0002 12:28
 **/
@Data
public class BannerDTO {

    private Integer bannerId;

    /** Banner名称，通常作为标识. */
    private String bannerName;

    /** Banner描述. */
    private String bannerDescription;

    List<BannerItem> bannerItemList;
}
