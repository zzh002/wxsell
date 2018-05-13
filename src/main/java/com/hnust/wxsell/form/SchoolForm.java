package com.hnust.wxsell.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class SchoolForm {
    @NotEmpty(message = "token不能为空")
    private String token;

    private String schoolId;

    @NotEmpty(message = "学校名称不能为空")
    private String schoolName;

    @NotEmpty(message = "学校编号不能为空")
    private String schoolNo;

    private String provinceNo;

    private String cityNo;
}
