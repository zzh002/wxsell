package com.hnust.wxsell.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZZH
 * @date 2018/6/20 0020 10:12
 **/
@Data
public class SellerRankVo implements Serializable {

    private static final long serialVersionUID = 9217071427832628031L;

    private String schoolNo;

    private Integer rank;
}
