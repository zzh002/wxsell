package com.hnust.wxsell.enums;

import lombok.Getter;

/**
 * @author ZZH
 * @date 2018/4/10 0010 19:22
 **/
@Getter
public enum SellerRank implements CodeEnum {
    DISTRIBUTOR(0,"配送员"),
    ADMIN(1,"管理员"),
    ;

    private Integer code;

    private String message;
    SellerRank(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
