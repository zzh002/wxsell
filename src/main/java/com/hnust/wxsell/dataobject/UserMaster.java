package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户主表
 */

@Data
@Entity
public class UserMaster {

    @Id
    private String userId;

    //寝室openid
    private String openId;

    //学校编号
    private String schoolNo;

    //寝室编号
    private String groupNo;

}
