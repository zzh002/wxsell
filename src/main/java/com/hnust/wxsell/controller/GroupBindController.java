package com.hnust.wxsell.controller;

import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.dataobject.UserMaster;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.UserMasterService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


/**
 * 用户绑定寝室
 * @author ZZH
 * @date 2018/4/7 0007 13:26
 **/
@Controller
@RequestMapping("/group/buyer")
@Slf4j
public class GroupBindController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private UserMasterService userMasterService;
    @Autowired
    private UserTokenService userTokenService;


    /**
     * 创建寝室二维码
     * @param resp
     * @param groupNo
     * @param token
     * @throws IOException
     */
    @RequestMapping(value = "/create", method = { RequestMethod.POST, RequestMethod.GET })
    public void create(HttpServletResponse resp,
                       @RequestParam("groupNo") String groupNo,
                       @RequestParam("token") String token) throws IOException {

        resp.addHeader("content-type","image/png");
        String groupAndschool = groupNo + "?" + userTokenService.getUserMaster(token).getSchoolNo();
        //   log.info("【二维码】，groupAndschool={}",groupAndschool);
        String url = projectUrlConfig.getSell()
                +"/sell/group/buyer/bindAuthorize?groupAndschool="
                +groupAndschool;

        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                stream = resp.getOutputStream();
                QRCodeUtil.encode(url,stream);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }

    /**
     * 用户绑定寝室
     * @param groupAndschool
     * @return
     */
    @GetMapping("/bindAuthorize")
    public String bindAuthorize(@RequestParam("groupAndschool") String groupAndschool) {
        //1. 配置
        //2. 调用方法
        String url = projectUrlConfig.getWechatMpAuthorize() + "/sell/group/buyer/bindUserInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(groupAndschool
        ));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/bindUserInfo")
    public String bindUserInfo(@RequestParam("code") String code,
                                     @RequestParam("state") String groupAndSchool) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        String groupNo = new String();
        String schoolNo = new String();

        for (int i=0 ;i<groupAndSchool.length() ;i++){
            if (groupAndSchool.substring(i,i+1).equals("?")){
                groupNo = groupAndSchool.substring(0,i);
                schoolNo = groupAndSchool.substring(i+1,groupAndSchool.length());
            }
        }
        log.info("【二维码】，groupNo={},schoolNo={},groupAndschool={}"
                ,groupNo,schoolNo,groupAndSchool);
        UserMaster userMaster = new UserMaster();
        userMaster.setOpenId(openId);
        userMaster.setGroupNo(groupNo);
        userMaster.setSchoolNo(schoolNo);

        try {
            userMasterService.save(userMaster);
        }catch (SellException e){
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR);
        }

        //TODO
      return "redirect:" + projectUrlConfig.getSell() + "/buyerInterface/replenishment.html";

    }

    /**
     * 注册管理员账号
     * @param schoolAndrank
     * @return
     */
    @GetMapping("/adminAuthorize")
    public String adminAuthorize(@RequestParam("schoolAndrank") String schoolAndrank) {
        //1. 配置
        //2. 调用方法
        String url = projectUrlConfig.getWechatMpAuthorize() + "/sell/group/buyer/adminUserInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(schoolAndrank));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/adminUserInfo")
    public String adminUserInfo(@RequestParam("code") String code,
                               @RequestParam("state") String schoolAndrank) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        String headImgUrl = new String();
        String nickname = new String();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            headImgUrl = wxMpUser.getHeadImgUrl();
            nickname = wxMpUser.getNickname();
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        String schoolNo = new String();
        String rank = new String();
        for (int i=0 ;i<schoolAndrank.length() ;i++){
            if (schoolAndrank.substring(i,i+1).equals("?")){
                schoolNo = schoolAndrank.substring(0,i);
                rank = schoolAndrank.substring(i+1,schoolAndrank.length());
            }
        }
        log.info("【二维码】，schoolNo={},rank={},schoolAndrank={}"
                ,schoolNo,rank,schoolAndrank);

        //TODO
        return "redirect:" + projectUrlConfig.getSell() + "/sell/register.html?"
                + "openid=" + openId
                + "&schoolNo=" + schoolNo
                + "&rank=" + Integer.parseInt(rank)
                + "&nickname=" +URLEncoder.encode(nickname)
                + "&headImgUrl=" +headImgUrl;

    }
}
