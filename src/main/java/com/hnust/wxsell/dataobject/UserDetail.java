package com.hnust.wxsell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户详细信息表
 */

@Data
@Entity
public class UserDetail {

    @Id
    private String id;

    private String userId;

    //用户姓名
    private String userName;

    //用户电话
    private String userPhone;

    //用户openid
    private String openId;

    //性别
    private String sex;

    //昵称
    private String nickname;

    //头像
    private String headImgUrl;

    //省编号
    private String provinceNo;

    //城市编号
    private String cityNo;

    //地区
    private String country;

    //详细地址
    private String detailAddress;

    //扩展信息
    private String extend;

}
