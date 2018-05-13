package com.hnust.wxsell.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hnust.wxsell.config.ProjectUrlConfig;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 **/
@Controller
@RequestMapping("/seller/qrcode")
@Slf4j
public class SellerQrCodeController {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 创建寝室绑定二维码
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
        String groupAndschool = groupNo + "?" + userTokenService.getSellerInfo(token).getSchoolNo();
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
     * 创建管理员注册二维码
     * @param resp
     * @param token
     * @throws IOException
     */
    @RequestMapping(value = "/createAdmin", method = { RequestMethod.POST, RequestMethod.GET })
    public void createAdmin(HttpServletResponse resp,
                       @RequestParam("token") String token) throws IOException {

        //http://chibei.s1.natapp.cc域名去配置文件里配置
//        String url = "http://chibei.s1.natapp.cc/sell/seller/login?openid=oNh_M1A94KKKfFK_wEjptukfKFQs";


        resp.addHeader("content-type","image/png");
        String rank = "0";
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo.getRank().equals(0)){
            log.error("【创建注册管理员二维码】，权限错误");
            throw new SellException(ResultEnum.ADMIN_PERMISSION_ERROR.getCode(),
                    ResultEnum.ADMIN_PERMISSION_ERROR.getMessage() );
        }
        else if (sellerInfo.getRank().equals(1)){
            if (sellerInfo.getSellerId().equals("admin")){
                rank = "1";
            }
        }
        String schoolAndrank = sellerInfo.getSchoolNo() + "?" +rank;
        //   log.info("【二维码】，groupAndschool={}",groupAndschool);
        String url = projectUrlConfig.getSell()
                +"/sell/group/buyer/adminAuthorize?schoolAndrank="
                +schoolAndrank;

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
}
