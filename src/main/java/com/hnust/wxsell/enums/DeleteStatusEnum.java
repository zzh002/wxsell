package com.hnust.wxsell.enums;

import lombok.Getter;


@Getter
public enum DeleteStatusEnum implements CodeEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除"),
    ;

    private Integer code;

    private String message;

    DeleteStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
