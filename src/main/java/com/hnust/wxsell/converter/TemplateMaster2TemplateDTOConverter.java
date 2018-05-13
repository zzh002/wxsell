package com.hnust.wxsell.converter;

import com.hnust.wxsell.dataobject.TemplateMaster;
import com.hnust.wxsell.dto.TemplateDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/10 0010 14:47
 **/
public class TemplateMaster2TemplateDTOConverter {
    public static TemplateDTO convert(TemplateMaster templateMaster) {

        TemplateDTO templateDTO = new TemplateDTO();
        BeanUtils.copyProperties(templateMaster, templateDTO);
        return templateDTO;
    }

    public static List<TemplateDTO> convert(List<TemplateMaster> templateMasterList) {
        return templateMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
