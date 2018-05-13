package com.hnust.wxsell.converter;

import com.hnust.wxsell.dataobject.DispatchMaster;
import com.hnust.wxsell.dto.DispatchDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/10 0010 14:04
 **/
public class DispatchMaster2DispatchDTOConverter {

    public static DispatchDTO convert(DispatchMaster dispatchMaster) {

        DispatchDTO dispatchDTO = new DispatchDTO();
        BeanUtils.copyProperties(dispatchMaster, dispatchDTO);
        return dispatchDTO;
    }

    public static List<DispatchDTO> convert(List<DispatchMaster> dispatchMasterList) {
        return dispatchMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
