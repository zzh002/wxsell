package com.hnust.wxsell.utils;

import com.hnust.wxsell.enums.CodeEnum;

/**
 * Create by HJT
 * 2018/3/19 11:16
 **/
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
