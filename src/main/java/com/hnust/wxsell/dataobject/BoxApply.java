package com.hnust.wxsell.dataobject;


import com.hnust.wxsell.enums.DeleteStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


/**
 * 盒子申请表单
 */
@Entity
@Data
@DynamicUpdate
public class BoxApply {
    @Id
    private String id;

    /**姓名 */
    private String userName;

    /**电话 */
    private String userPhone;

    /**省编号*/
    private String provinceNo;

    /**城市编号*/
    private String cityNo;

    /** 学校编号 */
    private String schoolNo;

    /**宿舍区号 */
    private String groupDistrict;

    /**宿舍编号 */
    private String groupNo;

    /**删除状态,默认未删除 */
    private Integer deleteStatus = DeleteStatusEnum.NOT_DELETED.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
