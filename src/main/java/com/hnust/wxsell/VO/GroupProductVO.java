package com.hnust.wxsell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 寝室商品（包含类目）
 **/

@Data
public class GroupProductVO implements Serializable {

    private static final long serialVersionUID = 6291685508799839005L;

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<GroupProductInfoVO> groupProductInfoVOList;
}
