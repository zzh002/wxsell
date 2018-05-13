package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.contant.RedisConstant;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.SellerInfoRepository;
import com.hnust.wxsell.repository.UserMasterRepository;
import com.hnust.wxsell.service.UserTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ZZH
 * @date 2018/4/5 0005 13:15
 **/
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public String getTokenValue(String token) {
        if (StringUtils.isBlank(token)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }

        //去redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.
                format(RedisConstant.TOKEN_PREFIX, token));
        if (StringUtils.isEmpty(tokenValue)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }
        return tokenValue;
    }

    @Override
    public UserMaster getUserMaster(String token) {
        if (StringUtils.isBlank(token)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }

        //去redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.
                format(RedisConstant.TOKEN_PREFIX, token));
        if (StringUtils.isEmpty(tokenValue)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }
        //查找用户
        UserMaster userMaster = userMasterRepository.findByOpenId(tokenValue);
        if (userMaster==null){
            throw new SellException(ResultEnum.USER_NOT_EXIST);
        }
        return userMaster;
    }

    @Override
    public String setToken(String openId) {
        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token),
                openId, expire, TimeUnit.SECONDS);

        return  token;
    }

    @Override
    public SellerInfo getSellerInfo(String token) {
        if (StringUtils.isBlank(token)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }

        //去redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.
                format(RedisConstant.TOKEN_PREFIX, token));
        if (StringUtils.isEmpty(tokenValue)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }
        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid(tokenValue);
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_NOT_EXIST);
        }

        return sellerInfo;
    }
}
