package com.hnust.wxsell.controller;


import com.hnust.wxsell.VO.ResultVO;
import com.hnust.wxsell.dataobject.School;
import com.hnust.wxsell.dataobject.SellerInfo;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.form.SchoolForm;
import com.hnust.wxsell.service.SchoolService;
import com.hnust.wxsell.service.UserTokenService;
import com.hnust.wxsell.utils.KeyUtil;
import com.hnust.wxsell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/seller/school")
@Slf4j
public class SellerSchoolController {

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserTokenService userTokenService;

    /**
     * 学校列表
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(){
        List<School> schoolList = schoolService.findAll();
        return ResultVOUtil.success(schoolList);
    }

    /**
     * 学校详情
     * @param schoolNo
     * @return
     */
    @GetMapping("/detail")
    public ResultVO detail(@RequestParam("schoolNo") String schoolNo){
        School school = schoolService.findBySchoolNo(schoolNo);
        return ResultVOUtil.success(school);
    }

    /**
     * 保存/更新学校
     * @param schoolForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ResultVO save(@Valid SchoolForm schoolForm,
                         BindingResult bindingResult){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(schoolForm.getToken());
        if (sellerInfo == null){
            return ResultVOUtil.error(ResultEnum.TOKEN_ERROR.getCode(),
                    ResultEnum.TOKEN_ERROR.getMessage());
        }
        if (bindingResult.hasErrors()) {
            return ResultVOUtil.error(1003,"参数错误");
        }
        School school = new School();
        if (!StringUtils.isEmpty(schoolForm.getSchoolId())){
            school = schoolService.findOne(schoolForm.getSchoolId());
        } else {
            schoolForm.setSchoolId(KeyUtil.genUniqueKey());
        }
        BeanUtils.copyProperties(schoolForm,school);
        schoolService.save(school);
        return ResultVOUtil.success();
    }

    /**
     * 删除学校
     * @param schoolId
     * @param token
     * @return
     */
    @GetMapping("/delete")
    public ResultVO delete(@RequestParam("schoolId") String schoolId,
                           @RequestParam("token") String token){
        SellerInfo sellerInfo = userTokenService.getSellerInfo(token);
        if (sellerInfo == null){
            return ResultVOUtil.error(ResultEnum.TOKEN_ERROR.getCode(),
                    ResultEnum.TOKEN_ERROR.getMessage());
        }
        School school = schoolService.findOne(schoolId);
        schoolService.delete(school);
        return ResultVOUtil.success();
    }

}
