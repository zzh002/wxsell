package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/6/14 0014 14:37
 **/
@Data
public class TemplateForm {
    @NotEmpty(message = "商品不能为空")
    private String items;

    /** 模板名字 */
    @NotEmpty(message = "模板名不能为空")
    private String templateName;
}
