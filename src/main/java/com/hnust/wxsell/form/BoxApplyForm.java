package com.hnust.wxsell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author ZZH
 * @date 2018/4/2 0002 13:07
 **/
@Data
public class BoxApplyForm {

    /**姓名 */
    @NotEmpty(message = "姓名不能为空")
    private String userName;

    /**电话 */
    @NotEmpty(message = "电话不能为空")
    private String userPhone;

    /**省编号*/
    private String provinceNo;

    /**城市编号*/
    private String cityNo;

    /** 学校编号 */
    @NotEmpty(message = "学校不能为空")
    private String schoolNo;

    /**宿舍区号 */
    @NotEmpty(message = "宿舍区号不能为空")
    private String groupDistrict;

    /**宿舍编号 */
    @NotEmpty(message = "宿舍编号不能为空")
    private String groupNo;
}
