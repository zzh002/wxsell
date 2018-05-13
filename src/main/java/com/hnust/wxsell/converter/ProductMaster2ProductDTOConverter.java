package com.hnust.wxsell.converter;

import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dto.ProductDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/6 0006 17:11
 **/
public class ProductMaster2ProductDTOConverter {

    public static ProductDTO convert(ProductMaster productMaster) {

        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productMaster, productDTO);
        return productDTO;
    }

    public static List<ProductDTO> convert(List<ProductMaster> productMasterList) {
        return productMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
