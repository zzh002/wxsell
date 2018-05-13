package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/8 0008 20:28
 **/
@Data
public class OrderForm {
    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
