package com.hnust.wxsell.form;

import lombok.Data;

/**
 * @author ZZH
 * @date 2018/4/11 0011 20:29
 **/
@Data
public class CategoryForm {
    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;
}
