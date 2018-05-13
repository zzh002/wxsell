package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.ProductDistrict;
import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.enums.ProductStatusEnum;
import com.hnust.wxsell.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/6 0006 20:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    public void findOne() throws Exception{
        ProductDTO productDTO = productService.findOne("123456");
        log.info("productDTO={}",productDTO);
        Assert.assertEquals("123456", productDTO.getProductId());
    }

    @Test
    public void findUpAll() {
        List<ProductDTO> productDTOList = productService.findUpAll("1");
        log.info("productDTOList={}",productDTOList);

            Assert.assertNotEquals(null, productDTOList.get(0));

    }

    @Test
    public void findAll() {
        PageRequest request = new PageRequest(0, 2);
        Page<ProductDTO> productDTOPage = productService.findAll("1",request);
        log.info("productDTOPage={}",productDTOPage.getTotalElements());
        Assert.assertNotEquals(0, productDTOPage.getTotalElements());
    }

    @Test
    public void findByCategoryType() {
        List<ProductDTO> productDTOList = productService.findByCategoryType(1,"1");
        log.info("productDTOList={}",productDTOList);
        Assert.assertNotEquals(null, productDTOList.get(0));
    }

    @Test
    public void save() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId("123458");
        productDTO.setCategoryType(3);
        productDTO.setProductName("可乐");
        productDTO.setProductPrice(new BigDecimal(3.5));
        productDTO.setProductDescription("好喝的饮料");
        List<ProductDistrict> productDistrictList = new ArrayList<>();
        ProductDistrict productDistrict = new ProductDistrict();
        productDistrict.setId("128");
        productDistrict.setProductId("123458");
        productDistrict.setProductStock(88);
        productDistrict.setSchoolNo("1");
        productDistrictList.add(productDistrict);
        productDTO.setProductDistrictList(productDistrictList);

        ProductDTO result = productService.save(productDTO);
        Assert.assertNotNull(result);
    }

    @Test
    public void increaseStock() {

    }

    @Test
    public void decreaseStock() {
    }

    @Test
    public void onSale() {
        ProductDTO  result = productService.onSale("123456","1");
        Assert.assertNotNull(result);
    }

    @Test
    public void offSale() {
        ProductDTO  result = productService.offSale("123456","1");
        Assert.assertNotNull(result);
    }
}