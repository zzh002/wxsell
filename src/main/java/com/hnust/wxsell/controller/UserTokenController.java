package com.hnust.wxsell.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.google.gson.JsonObject;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.contant.RedisConstant;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.UserMasterService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.JsonUtil;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ZZH
 * @date 2018/4/5 0005 13:26
 **/
@RestController
@RequestMapping("/token")
@Slf4j
public class UserTokenController {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMasterService userMasterService;

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入auth方法。。。");
        log.info("code={}", code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx288322c27892a46e&secret=c03f62364d97ec5f355343a00ca99e0a&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);
    }

    /**
     * 小程序客户端获取token口令
     * @param getCode
     * @return
     */
    @PostMapping(value = "/MaCreate")
    public String token(@RequestBody String getCode,
                        HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        // 获取json中的某个属性,3步 1.获取转换器 2.转换成jsonElement 3.转换成jsonObject
        String code = JsonUtil.getKeyValue(getCode,"code");

        if (StringUtils.isBlank(code)) {
            throw new SellException(ResultEnum.TOKEN_ERROR);
        }

        WxMaJscode2SessionResult session = new WxMaJscode2SessionResult();
        try {
            session = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            throw new SellException(ResultEnum.TOKEN_ERROR.getCode(), e.getError().getErrorMsg());
        }

        UserMaster userMaster = userMasterService.findByOpenid(session.getOpenid());
        if (userMaster==null){
            userMaster = new UserMaster();
            userMaster.setOpenId(session.getOpenid());
            userMaster.setUserId(KeyUtil.genUniqueKey());
            userMasterService.save(userMaster);
        }
        //2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token),
                userMaster.getUserId(), expire, TimeUnit.SECONDS);

        //向客户端返回token
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token",token);
        return JsonUtil.toJson(jsonObject);
    }

    /**
     * 验证token合法性
     * @param getToken
     * @return
     */
    @PostMapping(value = "/verify")
    public ResultVO verify(@RequestBody String getToken,
                           HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Methods","GET,POST,OPTIONS,DELETE,PUT");
        String token = JsonUtil.getKeyValue(getToken,"token");
        userTokenService.getTokenValue(token);

        return ResultVOUtil.success();
    }

}
