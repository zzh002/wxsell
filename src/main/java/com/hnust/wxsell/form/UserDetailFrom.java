package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/5 0005 14:13
 **/
@Data
public class UserDetailFrom {
    //用户姓名
    @NotEmpty(message = "姓名必填")
    private String userName;

    //用户电话
    @NotEmpty(message = "电话必填")
    private String userPhone;

    //省编号
    @NotEmpty(message = "省必填")
    private String provinceNo;

    //城市编号
    @NotEmpty(message = "市必填")
    private String cityNo;

    //地区
    @NotEmpty(message = "地区必填")
    private String country;

    //详细地址
    @NotEmpty(message = "详细地址必填")
    private String detailAddress;
}
