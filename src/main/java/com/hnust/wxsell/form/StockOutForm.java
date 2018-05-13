package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/13 0013 12:54
 **/
@Data
public class StockOutForm {

    @NotEmpty(message = "回收商品不能为空")
    private String items;
}
