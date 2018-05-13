package com.hnust.wxsell.controller;

import com.hnust.wxsell.VO.PageListVO;
import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.BoxApply;
import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.service.BoxApplyService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ZZH
 * @date 2018/4/11 0011 20:01
 **/
@RestController
@RequestMapping("/seller/boxApply")
public class SellerBoxApplyController {
    @Autowired
    BoxApplyService boxApplyService;
    @Autowired
    private UserTokenService userTokenService;

    /**
     * 申请列表
     * @param page
     * @param size
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                         @RequestParam(value = "size", defaultValue = "100") Integer size,
                         @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo==null){
            throw new SellException(ResultEnum.SELLER_NOT_EXIST);
        }

        PageRequest request = new PageRequest(page - 1, size);
        Page<BoxApply> boxApplyPage =  boxApplyService.findExistAllBySchoolNo
                (request,sellerInfo.getSchoolNo());

        PageListVO pageListVO = new PageListVO();
        pageListVO.setList(boxApplyPage.getContent());
        pageListVO.setSize(size);
        pageListVO.setCurrentPage(page);
        return ResultVOUtil.success(pageListVO);
    }

    /**
     * 申请详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("id") String id){

        BoxApply boxApply = boxApplyService.findOne(id);

        return ResultVOUtil.success(boxApply);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("delete")
    public ResultVO delete(@RequestParam("id") String id){

        try {
            BoxApply boxApply = boxApplyService.delete(id);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return ResultVOUtil.success();
    }

    /**
     * 保存
     * @param id
     * @return
     */
    @GetMapping("save")
    public ResultVO save(@RequestParam("id") String id){

        try {
            GroupMaster groupMaster = boxApplyService.save(id);
            BoxApply boxApply = boxApplyService.delete(id);
        }catch (SellException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        return ResultVOUtil.success();
    }
}
