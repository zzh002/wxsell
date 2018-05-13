package com.hnust.wxsell.service;

import com.hnust.wxsell.dataobject.ProductDistrict;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 13:47
 **/
public interface ProductService {

    List<ProductDTO> findByProductStatusAndProductIdIn(String schoolNo ,List<String> productIdList);

    ProductDTO findOne(String productId);

    ProductDTO findOne(String productId , String schoolNo);

    /**查询所有在架商品列表*/
    List<ProductDTO> findUpAll(String schoolNo);

    List<ProductDTO> findAll(String schoolNo);

    /**卖家端分页查找商品 */
    Page<ProductDTO> findAll(String schoolNo,Pageable pageable);

    /**卖家端查询类目下所有商品*/
    List<ProductDTO> findByCategoryType(Integer category , String schoolNo);

    ProductDTO save(ProductDTO productDTO);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList ,String schoolNo);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList , String schoolNo);

    //上架
    ProductDTO onSale(String productId , String schoolNo);

    //下架
    ProductDTO offSale(String productId, String schoolNo);
}
