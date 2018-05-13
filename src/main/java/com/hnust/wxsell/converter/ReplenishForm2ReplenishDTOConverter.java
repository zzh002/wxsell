package com.hnust.wxsell.converter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.ReplenishFrom;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/8 0008 21:33
 **/
@Slf4j
public class ReplenishForm2ReplenishDTOConverter {
    public static ReplenishDTO convert(String replenish){

        ReplenishDTO replenishDTO = new ReplenishDTO();

        Gson gson = new Gson();

        List<ReplenishDetail> replenishDetailList = new ArrayList<>();

        //先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(replenish).getAsJsonObject();
        //再转JsonArray 加上数据头
        JsonArray jsonArray = jsonObject.getAsJsonArray("products");

        //循环遍历
        for (JsonElement product : jsonArray) {
            //通过反射 得到UserBean.class
            ReplenishDetail replenishDetail = new ReplenishDetail();

            try{
                replenishDetail = gson.fromJson(product, new TypeToken<ReplenishDetail>() {}.getType());
            }catch (Exception e){
                log.error("【对象装换】错误， string={}", product);
                throw new SellException(ResultEnum.PARAM_ERROR);
            }

            replenishDetailList.add(replenishDetail);
        }

        replenishDTO.setReplenishDetailList(replenishDetailList);

        return replenishDTO;
    }

    public static ReplenishDTO convert(ReplenishFrom replenishFrom) {
        Gson gson = new Gson();
        ReplenishDTO replenishDTO = new ReplenishDTO();

        List<ReplenishDetail> replenishDetailList = new ArrayList<>();
        try {
            replenishDetailList = gson.fromJson(replenishFrom.getItems(),
                    new TypeToken<List<ReplenishDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", replenishFrom.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        replenishDTO.setReplenishDetailList(replenishDetailList);

        return replenishDTO;
    }
}
