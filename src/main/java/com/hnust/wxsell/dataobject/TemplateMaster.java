package com.hnust.wxsell.dataobject;


import com.hnust.wxsell.enums.DeleteStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class TemplateMaster {

    /** 模板id. */
    @Id
    private String templateId;

    /** 模板名字 */
    private String templateName;

    /** 学校编号*/
    private String schoolNo;

    /**删除状态,默认未删除 */
    private Integer deleteStatus = DeleteStatusEnum.NOT_DELETED.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
