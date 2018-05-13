package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 城市列表
 */
@Entity
@Data
public class City {

    @Id
    private String cityId;

    //城市名称
    private String cityName;

    //城市编号
    private String cityNo;

    //省编号
    private String provinceNo;


}
