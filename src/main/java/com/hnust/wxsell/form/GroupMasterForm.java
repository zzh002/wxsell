package com.hnust.wxsell.form;

import lombok.Data;

/**
 * @author ZZH
 * @date 2018/4/11 0011 22:00
 **/
@Data
public class GroupMasterForm {
    private String groupId;

    private String groupNo;

    private String schoolNo;
    /** 宿舍分区. */
    private String groupDistrict;

    /** 申请人姓名. */
    private String userName;

    /** 申请人电话. */
    private String userPhone;
}
