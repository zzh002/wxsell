package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.UserMaster;

/**
 * @author ZZH
 * @date 2018/4/5 0005 13:10
 **/
public interface UserTokenService {

    //获取token值
    String getTokenValue(String token);

    //根据token查询用户
    UserMaster getUserMaster(String token);

    //设置token
    String setToken(String openId);

    //根据token查询卖家管理员
    SellerInfo getSellerInfo(String token);

}
