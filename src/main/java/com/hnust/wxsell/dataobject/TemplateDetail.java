package com.hnust.wxsell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TemplateDetail {
    @Id
    private String detailId;

    /** 模板id. */
    private String templateId;

    /** 商品id. */
    private String productId;

    /** 商品数量. */
    private Integer productQuantity;

    /** 商品名称. */
    private String productName;
}
