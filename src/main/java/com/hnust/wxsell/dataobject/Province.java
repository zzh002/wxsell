package com.hnust.wxsell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Province {

    @Id
    private String provinceId;

    private String provinceName;

    private String provinceNo;

}
