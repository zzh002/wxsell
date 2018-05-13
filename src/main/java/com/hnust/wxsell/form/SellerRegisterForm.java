package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/10 0010 20:08
 **/
@Data
public class SellerRegisterForm {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "openid不能为空")
    private String openid;

    @NotEmpty(message = "卖家级别不能为空")
    private Integer rank;

    @NotEmpty(message = "学校编号")
    private String schoolNo;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "电话不能为空")
    private String phone;
}
