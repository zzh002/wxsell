package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ThemeProduct {

    @Id
    private String id;

    /**主题外键*/
    private Integer themeId;

    /**商品外键*/
    private String productId;
}
