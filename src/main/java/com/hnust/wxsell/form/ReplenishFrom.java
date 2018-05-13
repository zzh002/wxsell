package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/8 0008 21:35
 **/
@Data
public class ReplenishFrom {

   // @NotEmpty(message = "寝室编号不能为空")
    private String groupNo;

    @NotEmpty(message = "补货不能为空")
    private String items;
}
