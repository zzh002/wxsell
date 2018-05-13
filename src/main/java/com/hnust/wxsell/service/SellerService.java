package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.form.SellerLoginForm;
import com.hnust.wxsell.form.SellerRegisterForm;

/**
 * @author ZZH
 * @date 2018/4/10 0010 19:35
 **/
public interface SellerService {
    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);

    SellerInfo sellerlogin(SellerLoginForm sellerLoginForm);

    SellerInfo save(SellerRegisterForm sellerRegisterForm);

    SellerInfo update(SellerInfo sellerInfo);
}
