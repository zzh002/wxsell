package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.DispatchDTO;

import java.util.List;

/**
 * 寝室商品
 * @author ZZH
 * @date 2018/4/7 0007 16:22
 **/
public interface GroupProductService {

    /** 查询该寝室所有商品列表. */
    List<GroupProduct> findBySchoolNoAndGroupNo(String schoolNo , String groupNo);

    GroupProduct findBySchoolNoAndGroupNoAndProductId(String schoolNo ,
                                                      String groupNo, String productId);

    GroupProduct save(GroupProduct groupProduct);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList, String groupNo ,String schoolNo);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList, String groupNo ,String schoolNo);

    //补货
    void replenish(DispatchDTO dispatchDTO);

    //清空商品表
    void removeAll(GroupProduct groupProduct);

    //加销量
    void increaseSales(List<CartDTO> cartDTOList, String groupNo ,String schoolNo);

    //减销量
    void decreaseSales(List<CartDTO> cartDTOList, String groupNo ,String schoolNo);
}
