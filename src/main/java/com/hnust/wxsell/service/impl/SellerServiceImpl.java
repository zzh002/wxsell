package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.SellerLoginForm;
import com.hnust.wxsell.form.SellerRegisterForm;
import com.hnust.wxsell.repository.SellerInfoRepository;
import com.hnust.wxsell.service.SellerService;
import com.hnust.wxsell.utils.KeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZZH
 * @date 2018/4/10 0010 19:37
 **/
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;
    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }

    @Override
    public SellerInfo sellerlogin(SellerLoginForm sellerLoginForm) {

        //判断seller是否存在
        SellerInfo sellerInfo = repository.findByOpenid(sellerLoginForm.getOpenid());
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_LOGIN_ERROR);
        }
        if(!StringUtils.equals(sellerInfo.getUsername(), sellerLoginForm.getUsername())
                || !StringUtils.equals(sellerInfo.getPassword(), sellerLoginForm.getPassword())){
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        return sellerInfo;
    }

    @Override
    public SellerInfo save(SellerRegisterForm sellerRegisterForm) {
        SellerInfo sellerInfo = repository.findByOpenid(sellerRegisterForm.getOpenid());
        //判断新增卖家还是修改信息
        if (sellerInfo==null){
            sellerInfo = new SellerInfo();
            sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        }
        BeanUtils.copyProperties(sellerRegisterForm,sellerInfo);
        return repository.save(sellerInfo);
    }

    @Override
    public SellerInfo update(SellerInfo sellerInfo) {
        return repository.save(sellerInfo);
    }
}
