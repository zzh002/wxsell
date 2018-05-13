package com.hnust.wxsell.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hnust.wxsell.dataobject.TemplateDetail;
import com.hnust.wxsell.enums.DeleteStatusEnum;
import com.hnust.wxsell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Create by HJT
 * 2018/3/19 11:11
 **/
@Data
public class TemplateDTO {

    private String templateId;

    /** 模板名字 */
    private String templateName;

    /** 学校编号*/
    private String schoolNo;

    /**删除状态,默认未删除 */
    private Integer deleteStatus = DeleteStatusEnum.NOT_DELETED.getCode();

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<TemplateDetail> templateDetailList;

}
