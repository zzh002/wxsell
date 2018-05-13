package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.BoxApply;
import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.form.BoxApplyForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZZH
 * @date 2018/4/2 0002 13:06
 **/
public interface BoxApplyService {
    /* 创建盒子 */
    BoxApply create(BoxApplyForm boxApplyForm);

    /* 删除盒子 */
    BoxApply delete(String id);

    /**查找盒子*/
    BoxApply findOne(String id);

    /* 查询单校区未删除盒子列表 */
    Page<BoxApply> findExistAllBySchoolNo(Pageable pageable,String schoolNo);

    /* 查询单校区单宿舍区未删除盒子列表 */
    Page<BoxApply> findExistAllBySchoolNoAndDistrict(Pageable pageable,String schoolNo,String groupDistrict);

    /* 保存申请盒子信息 */
    GroupMaster save(String id);
}
