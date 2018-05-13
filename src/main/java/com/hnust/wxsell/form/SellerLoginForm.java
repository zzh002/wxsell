package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/10 0010 19:36
 **/
@Data
public class SellerLoginForm {
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "openid不能为空")
    private String openid;
}
