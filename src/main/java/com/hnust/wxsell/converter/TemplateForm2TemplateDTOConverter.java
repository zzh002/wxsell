package com.hnust.wxsell.converter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hnust.wxsell.dataobject.ReplenishDetail;
import com.hnust.wxsell.dataobject.TemplateDetail;
import com.hnust.wxsell.dto.TemplateDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.form.TemplateForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/6/14 0014 14:40
 **/
@Slf4j
public class TemplateForm2TemplateDTOConverter {
    public static TemplateDTO convert(String template) {
        TemplateDTO templateDTO = new TemplateDTO();

        Gson gson = new Gson();

        List<TemplateDetail> templateDetailList = new ArrayList<>();

        //先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(template).getAsJsonObject();
        //再转JsonArray 加上数据头
        JsonArray jsonArray = jsonObject.getAsJsonArray("products");

        //循环遍历
        for (JsonElement product : jsonArray) {
            //通过反射 得到UserBean.class
            TemplateDetail templateDetail = new TemplateDetail();

            try{
                templateDetail = gson.fromJson(product, new TypeToken<TemplateDetail>() {}.getType());
            }catch (Exception e){
                log.error("【对象装换】错误， string={}", product);
                throw new SellException(ResultEnum.PARAM_ERROR);
            }

            templateDetailList.add(templateDetail);
        }

        templateDTO.setTemplateDetailList(templateDetailList);

        return templateDTO;
    }

    public static TemplateDTO convert(TemplateForm templateForm) {
        Gson gson = new Gson();
        TemplateDTO templateDTO = new TemplateDTO();

        List<TemplateDetail> templateDetailList = new ArrayList<>();
        try {
            templateDetailList = gson.fromJson(templateForm.getItems(),
                    new TypeToken<List<TemplateDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", templateForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        templateDTO.setTemplateDetailList(templateDetailList);

        return templateDTO;
    }
}
