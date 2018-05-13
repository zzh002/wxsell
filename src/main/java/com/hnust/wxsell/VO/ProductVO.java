package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品包含类目
 **/

@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 4786278545991929033L;

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("url")
    private String categoryImg;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
