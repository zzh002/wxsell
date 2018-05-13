package com.hnust.wxsell.dataobject;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class School {

    @Id
    private String schoolId;

    private String schoolName;

    private String schoolNo;

    private String provinceNo;

    private String cityNo;

}
