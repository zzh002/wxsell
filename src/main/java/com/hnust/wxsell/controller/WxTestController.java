package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.VO.TestVO;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZZH
 * @date 2018/4/15 0015 16:02
 **/
@RestController
@Slf4j
@RequestMapping("/test")
public class WxTestController {

    @GetMapping("/auth")
    public ResultVO auth(@RequestParam("openid") String openid,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("headImgUrl") String headImgUrl) {
        TestVO testVO = new TestVO();
        testVO.setHeadImgUrl(headImgUrl);
        testVO.setNickname(nickname);
        testVO.setOpenid(openid);
        return ResultVOUtil.success(testVO);

    }
}
