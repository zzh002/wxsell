package com.hnust.wxsell.enums;

import lombok.Getter;



@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    PRODUCT_NOT_EXIST(10, "商品不存在"),

    PRODUCT_STOCK_ERROR(11, "商品库存不正确"),

    ORDER_NOT_EXIST(12, "订单不存在"),

    ORDERDETAIL_NOT_EXIST(13, "订单详情不存在"),

    ORDER_STATUS_ERROR(14, "订单状态不正确"),

    ORDER_UPDATE_FAIL(15, "订单更新失败"),

    ORDER_DETAIL_EMPTY(16, "订单详情为空"),

    ORDER_PAY_STATUS_ERROR(17, "订单支付状态不正确"),

    CART_EMPTY(18, "购物车为空"),

    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),

    WECHAT_MP_ERROR(20, "微信公众账号方面错误"),

    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(21, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(22, "订单取消成功"),

    ORDER_FINISH_SUCCESS(23, "订单完结成功"),

    PRODUCT_STATUS_ERROR(24, "商品状态不正确"),

    LOGIN_FAIL(25, "登录失败, 登录信息不正确"),

    LOGOUT_SUCCESS(26, "登出成功"),

    REGISTER_ERROR(27, "注册失败"),

    CLEAN_GROUP_PRODUCT_ERROR(28,"清空寝室商品失败"),

    REPLENISH_NOT_EXIST(29, "补货定单不存在"),

    DECREASE_STOCK_ERROR(30, "减库存失败"),

    INCREASE_STOCK_STOCK_ERROR(31, "加库存失败"),

    CART_NOT_EXIST(32, "补货订单不存在"),

    GROUP_NOT_EXIST(33, "寝室不存在"),

    REPLENISH_CANCEL_SUCCESS(34, "补货订单取消成功"),

    REPLENISH_FINISH_SUCCESS(35, "补货订单完结成功"),

    GROUPPRODUCT_NOT_EXIST(36, "寝室商品不存在"),

    PRODUCTCATEGORY_EXIST(37, "商品类目已存在"),

    GROUPMASTER_EXIST(38, "寝室已存在"),

    BANNER_NOT_EXIST(39,"轮播图不存在"),

    TOKEN_ERROR(40, "Token无效或已过期"),

    USER_NOT_EXIST(41,"用户不存在"),

    USER_ADDRESS_NOT_EXIST(42,"用户地址不存在"),

    PAY_CREATE_ERROR(43,"支付订单创建错误"),

    BIND_SUCCESS(44,"绑定寝室成功"),

    BIND_FAIL(45, "绑定寝室失败, 信息不正确"),

    DISPATCH_NOT_EXIST(46,"配送订单不存在"),

    SELLER_REGISTER_SUCCESS(47, "卖家注册成功"),

    DELETE_STATUS_ERROR(48,"删除状态不正确"),

    BOX_APPLY_NOT_EXIST(49,"盒子申请订单不存在"),

    TEMPLATE_NOT_EXIST(50,"模板不存在"),

    TEMPLATE_UPDATE_FAIL(51,"模板更新失败"),

    WECHAT_ORDER_QUERY_ERROR(52,"微信查询订单支付状态错误"),

    WECHAT_NOTIFY_ERROR(53,"微信异步通知方面错误"),

    SELLER_LOGIN_ERROR(54,"卖家登录失败"),

    BOX_APPLY_SAVE_ERROR(55,"盒子申请订单保存错误"),

    PRODUCT_DISTRICT_NOT_EXIST(56,"商品分区信息不存在"),

    SELLER_REGISTER_FAIL(57, "卖家注册失败"),

    SELLER_NOT_EXIST(58,"卖家不存在"),

    SELLER_LOGOUT_FAIL(59,"卖家登出失败"),

    GROUP_PRODUCT_STOCKOUT_ERROR(60,"回收寝室商品错误"),

    ADMIN_PERMISSION_ERROR(61,"管理权限错误"),

    SELLER_UPDATE_ERROR(62,"密码更新错误"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}