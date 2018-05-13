package com.hnust.wxsell.dataobject;

import lombok.Data;
/**
 * banner管理表
 *
 *
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Banner {

    @Id
    @GeneratedValue
    private Integer bannerId;

    /** Banner名称，通常作为标识. */
    private String bannerName;

    /** Banner描述. */
    private String bannerDescription;
}
