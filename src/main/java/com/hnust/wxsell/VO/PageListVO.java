package com.hnust.wxsell.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZZH
 * @date 2018/4/11 0011 22:11
 **/
@Data
public class PageListVO<T> implements Serializable {

    private static final long serialVersionUID = -8322789849206588658L;
    /**当前页. */
    private Integer currentPage;

    /**当前页条目数. */
    private Integer size;

    private T list;
}
