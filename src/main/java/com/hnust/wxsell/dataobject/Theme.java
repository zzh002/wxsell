package com.hnust.wxsell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Theme {
    @Id
    private Integer themeId;

    /**专题名称*/
    private String name;

    /**专题描述*/
    private String description;

    /**主题图*/
    private String topicImg;

    /**专题列表页，头图*/
    private String headImg;
}
