package com.hnust.wxsell.dataobject;


import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicUpdate
public class GroupDistrict {
    /** 类目id. */
    @Id
    @GeneratedValue
    private Integer id;

    /** 分组名字. */
    private String districtName;

    /** 分组编号. */
    private String groupDistrict;

    /**学校编号 */
    private String schoolNo;

    private Date createTime;

    private Date updateTime;

    public GroupDistrict() {
    }

    public GroupDistrict(String districtName, String groupDistrict) {
        this.districtName = districtName;
        this.groupDistrict = groupDistrict;
    }
}
