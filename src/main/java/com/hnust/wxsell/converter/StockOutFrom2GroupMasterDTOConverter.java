package com.hnust.wxsell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.StockOutForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/13 0013 13:05
 **/
@Slf4j
public class StockOutFrom2GroupMasterDTOConverter {

    public static GroupMasterDTO convert(StockOutForm stockOutForm){
    Gson gson = new Gson();
    GroupMasterDTO groupMasterDTO = new GroupMasterDTO();

    List<GroupProduct> groupProductList = new ArrayList<>();
        try {
        groupProductList = gson.fromJson(stockOutForm.getItems(),
                new TypeToken<List<GroupProduct>>() {
                }.getType());
    } catch (Exception e) {
        log.error("【对象转换】错误, string={}", stockOutForm.getItems());
        throw new SellException(ResultEnum.PARAM_ERROR);
    }
        groupMasterDTO.setGroupProductList(groupProductList);

        return groupMasterDTO;
    }
}
