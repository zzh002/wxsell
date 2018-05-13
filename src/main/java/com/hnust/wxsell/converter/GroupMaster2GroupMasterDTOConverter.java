package com.hnust.wxsell.converter;

import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dto.GroupMasterDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 **/
public class GroupMaster2GroupMasterDTOConverter {

    public static GroupMasterDTO convert(GroupMaster groupMaster) {

        GroupMasterDTO groupMasterDTO = new GroupMasterDTO();
        BeanUtils.copyProperties(groupMaster, groupMasterDTO);
        return groupMasterDTO;
    }

    public static List<GroupMasterDTO> convert(List<GroupMaster> groupMasterList) {
        return groupMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }

}
