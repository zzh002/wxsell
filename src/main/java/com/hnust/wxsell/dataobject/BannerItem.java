package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * banner子项表
 */

@Entity
@Data
public class BannerItem {
    @Id
    @GeneratedValue
    private Integer id;

    /**轮播图. */
    private String imgUrl;

    /**执行关键字，根据不同的type含义不同.*/
    private String keyWord;

    /**跳转类型，可能导向商品，可能导向专题，可能导向其他。0，无导向；1：导向商品;2:导向专题.*/
    private Integer type;

    /**外键，关联banner表.*/
    private Integer bannerId;
}
