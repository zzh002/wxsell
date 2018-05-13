package com.hnust.wxsell.converter;

import com.hnust.wxsell.dataobject.ReplenishMaster;
import com.hnust.wxsell.dto.ReplenishDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/8 0008 21:22
 **/
public class ReplenishMaster2ReplenishDTOConverter {

    public static ReplenishDTO convert(ReplenishMaster replenishMaster) {

        ReplenishDTO replenishDTO = new ReplenishDTO();
        BeanUtils.copyProperties(replenishMaster, replenishDTO);
        return replenishDTO;
    }

    public static List<ReplenishDTO> convert(List<ReplenishMaster> replenishMasterList) {
        return replenishMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
