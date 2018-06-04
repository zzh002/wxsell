package com.hnust.wxsell.dto;

import lombok.Data;

/**
 * 排序DTO

 **/

@Data
public class SortDTO {
    //排序方式
    private String orderType;

    //排序字段
    private String orderField;

    public SortDTO(String orderType, String orderField) {
        this.orderType = orderType;
        this.orderField = orderField;
    }

    //默认为DESC排序
    public SortDTO(String orderField) {
        this.orderField = orderField;
        this.orderType = "desc";
    }
}
