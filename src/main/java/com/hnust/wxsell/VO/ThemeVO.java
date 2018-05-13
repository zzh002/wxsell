package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Create by HJT
 * 2018/3/19 11:16
 **/
@Data
public class ThemeVO {

    @JsonProperty("id")
    private Integer themeId;

    /**专题名称*/
    private String name;

    /**专题描述*/
    private String description;

    /**主题图*/
    private String topicImg;

    /**专题列表页，头图*/
    private String headImg;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
