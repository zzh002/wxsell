package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class SellerUpdateForm {
    @NotEmpty(message = "token不能为空")
    private String token;

    @NotEmpty(message = "旧密码不能为空")
    private String passwordOld;

    @NotEmpty(message = "新密码不能为空")
    private String passwordNew;
}
