package com.hnust.wxsell.utils;

import com.hnust.wxsell.dto.SortDTO;
import org.springframework.data.domain.Sort;

/**
 * 排序工具类
 * Create by HJT
 * 2018/2/21 9:33
 **/
public class SortUtil {

    public static Sort basicSort() {
        return basicSort("desc", "updateTime");
    }

    public static Sort basicSort(String orderType, String orderField) {
        Sort sort = new Sort(Sort.Direction.fromString(orderType), orderField);
        return sort;
    }

    public static Sort basicSort(SortDTO... dtos) {
        Sort result = null;
        for(int i=0; i<dtos.length; i++) {
            SortDTO dto = dtos[i];
            if(result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }
        return result;
    }
}
