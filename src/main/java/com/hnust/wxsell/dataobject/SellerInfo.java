package com.hnust.wxsell.dataobject;

import com.hnust.wxsell.enums.SellerRank;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class SellerInfo {
    @Id
    private String sellerId;

    private String username;

    private String password;

    private String openid;

    private String name;

    private String phone;

    private String schoolNo;

    private Integer rank= SellerRank.DISTRIBUTOR.getCode();
}
