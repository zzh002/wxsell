package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.VO.TokenVO;

import com.hnust.wxsell.contant.RedisConstant;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.enums.SellerRankEnum;
import com.hnust.wxsell.service.SellerService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ZZH
 * @date 2018/4/11 0011 15:41
 **/
@RestController
@RequestMapping("/seller")
public class SellerAdministratorController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserTokenService userTokenService;

    @GetMapping("/admin")
    public ResultVO admin(@RequestParam("openid") String openid,
                          HttpServletResponse response) {

        //1. openid去和数据库里的数据匹配
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null) {
            return ResultVOUtil.error(ResultEnum.SELLER_NOT_EXIST.getCode(),
                    ResultEnum.SELLER_NOT_EXIST.getMessage());
        }

        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token),
                openid, expire, TimeUnit.SECONDS);

        //3. 设置token至cookie
      //  CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token);
        tokenVO.setSchoolNo(sellerInfo.getSchoolNo());
        return ResultVOUtil.success(tokenVO);

    }

    @GetMapping("/logout")
    public ResultVO logout(@RequestParam("token") String token) {
        //1. 从cookie里查询
     //   Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
    //    if (cookie != null) {
            //2. 清除redis
            redisTemplate.opsForValue().getOperations().
                    delete(String.format(RedisConstant.TOKEN_PREFIX, token));

            //3. 清除cookie
         //  CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
           return ResultVOUtil.success();
      //  }
      //  return ResultVOUtil.error(ResultEnum.SELLER_LOGOUT_FAIL.getCode(),
       //         ResultEnum.SELLER_LOGOUT_FAIL.getMessage());

    }

    /**
     * 卖家列表
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo == null){
            return ResultVOUtil.error(ResultEnum.TOKEN_ERROR.getCode(),
                    ResultEnum.TOKEN_ERROR.getMessage());
        }
        if (sellerInfo.getRank().equals(SellerRankEnum.DISTRIBUTOR)){
            return ResultVOUtil.error(ResultEnum.SELLER_RANK_ERROR.getCode(),
                    ResultEnum.SELLER_RANK_ERROR.getMessage());
        }
        List<SellerInfo> sellerInfoList = sellerService.findBySchoolNo(sellerInfo.getSchoolNo());
        return ResultVOUtil.success(sellerInfoList);
    }

    /**
     * 卖家详情
     * @param token
     * @param sellerId
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("token") String token,
                           @RequestParam("sellerId") String sellerId){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo == null){
            return ResultVOUtil.error(ResultEnum.TOKEN_ERROR.getCode(),
                    ResultEnum.TOKEN_ERROR.getMessage());
        }
        if (sellerInfo.getRank().equals(SellerRankEnum.DISTRIBUTOR)){
            return ResultVOUtil.error(ResultEnum.SELLER_RANK_ERROR.getCode(),
                    ResultEnum.SELLER_RANK_ERROR.getMessage());
        }
        SellerInfo s = sellerService.findOne(sellerId);
        return ResultVOUtil.success(s);
    }

    /**
     * 删除卖家
     * @param token
     * @param sellerId
     * @return
     */
    @GetMapping("/delete")
    public ResultVO delete(@RequestParam("token") String token,
                           @RequestParam("sellerId") String sellerId){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo == null){
            return ResultVOUtil.error(ResultEnum.TOKEN_ERROR.getCode(),
                    ResultEnum.TOKEN_ERROR.getMessage());
        }
        if (sellerInfo.getRank().equals(SellerRankEnum.DISTRIBUTOR)){
            return ResultVOUtil.error(ResultEnum.SELLER_RANK_ERROR.getCode(),
                    ResultEnum.SELLER_RANK_ERROR.getMessage());
        }
        SellerInfo s = sellerService.findOne(sellerId);
        if (s == null || !s.getSchoolNo().equals(sellerInfo.getSchoolNo())){
            return ResultVOUtil.error(ResultEnum.SELLER_NOT_EXIST.getCode(),
                    ResultEnum.SELLER_NOT_EXIST.getMessage());
        }
       sellerService.delete(sellerId);
        return ResultVOUtil.success();
    }

}

