package com.hnust.wxsell.service;

import com.hnust.wxsell.dto.DispatchDTO;
import com.hnust.wxsell.dto.ReplenishDTO;
import com.hnust.wxsell.dto.TemplateDTO;

import java.util.List;

/**
 * 补货模板
 */
public interface TemplateService {

    /** 根据配送单，新增模板 */
    TemplateDTO save(DispatchDTO dispatchDTO);

    /** 删除模板 */
    TemplateDTO delete(String templateId);

    /** 模板列表 */
    List<TemplateDTO> findList(String schoolNo);

    /** 查找某个模板 */
    TemplateDTO findOne(String templateId);

    TemplateDTO findBygroupNo(String schoolNo , String groupNo);

    /** 根据模板生成补货单*/
    ReplenishDTO createReplenishByTemplate(String schoolNo ,String groupNo, String templateId);
}
