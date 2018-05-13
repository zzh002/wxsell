package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.converter.ProductMaster2ProductDTOConverter;
import com.hnust.wxsell.dataobject.ProductDistrict;
import com.hnust.wxsell.dataobject.ProductMaster;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.enums.ProductStatusEnum;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.ProductDistrictRepository;
import com.hnust.wxsell.repository.ProductMasterRepository;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/6 0006 13:51
 **/
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMasterRepository productMasterRepository;
    @Autowired
    private ProductDistrictRepository productDistrictRepository;


    @Override
    public List<ProductDTO> findByProductStatusAndProductIdIn(String schoolNo, List<String> productIdList) {
        List<ProductMaster> productMasterList = productMasterRepository.findByProductIdIn(productIdList);
        List<ProductDTO> productDTOList = new ArrayList<>();

        for (ProductMaster productMaster : productMasterList){
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(productMaster,productDTO);
            ProductDistrict productDistrict = productDistrictRepository.findByProductIdAndSchoolNo
                    (productDTO.getProductId(),schoolNo);
            List<ProductDistrict> productDistrictList = new ArrayList<>();
            if (productDistrict.getProductStatus().equals(0)) {
                productDistrictList.add(productDistrict);
                productDTO.setProductDistrictList(productDistrictList);
                productDTOList.add(productDTO);
            }
        }
        return productDTOList;
    }

    @Override
    public ProductDTO findOne(String productId) {

        ProductMaster productMaster = productMasterRepository.findOne(productId);
        if (productMaster==null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        List<ProductDistrict> productDistrictList = productDistrictRepository.
                findByProductId(productId);
        if (CollectionUtils.isEmpty(productDistrictList)) {
            throw new SellException(ResultEnum.PRODUCT_DISTRICT_NOT_EXIST);
        }

        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productMaster,productDTO);
        productDTO.setProductDistrictList(productDistrictList);
        return productDTO;
    }

    @Override
    public ProductDTO findOne(String productId, String schoolNo) {
        ProductMaster productMaster = productMasterRepository.findOne(productId);
        if (productMaster==null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        List<ProductDistrict> productDistrictList = new ArrayList<>();
        ProductDistrict productDistrict = productDistrictRepository.
                findByProductIdAndSchoolNo(productId,schoolNo);
        productDistrictList.add(productDistrict);
        if (CollectionUtils.isEmpty(productDistrictList)) {
            throw new SellException(ResultEnum.PRODUCT_DISTRICT_NOT_EXIST);
        }

        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productMaster,productDTO);
        productDTO.setProductDistrictList(productDistrictList);
        return productDTO;
    }

    @Override
    public List<ProductDTO> findUpAll(String schoolNo) {

        List<ProductDistrict> productDistrictList = productDistrictRepository.
                findBySchoolNoAndProductStatus(schoolNo, ProductStatusEnum.UP.getCode());
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductDistrict productDistrict :productDistrictList){
            ProductMaster productMaster = productMasterRepository.
                    findOne(productDistrict.getProductId());
            List<ProductDistrict> result = new ArrayList<>();
            result.add(productDistrict);
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(productMaster,productDTO);
            productDTO.setProductDistrictList(result);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public List<ProductDTO> findAll(String schoolNo) {
        List<ProductDistrict> productDistrictList = productDistrictRepository.
                findBySchoolNo(schoolNo);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductDistrict productDistrict :productDistrictList){
            ProductMaster productMaster = productMasterRepository.
                    findOne(productDistrict.getProductId());
            List<ProductDistrict> result = new ArrayList<>();
            result.add(productDistrict);
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(productMaster,productDTO);
            productDTO.setProductDistrictList(result);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public Page<ProductDTO> findAll(String schoolNo, Pageable pageable) {
        Page<ProductDistrict> productDistrictPage = productDistrictRepository.
                findBySchoolNo(schoolNo , pageable);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductDistrict productDistrict :productDistrictPage){
            ProductMaster productMaster = productMasterRepository.
                    findOne(productDistrict.getProductId());
            List<ProductDistrict> result = new ArrayList<>();
            result.add(productDistrict);
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(productMaster,productDTO);
            productDTO.setProductDistrictList(result);
            productDTOList.add(productDTO);
        }
        return new PageImpl<ProductDTO>(productDTOList, pageable,productDistrictPage.
                getTotalElements());
    }

    @Override
    public List<ProductDTO> findByCategoryType(Integer category , String schoolNo) {
        List<ProductMaster> productMasterList = productMasterRepository.findByCategoryType(category);
        List<ProductDTO> productDTOList = ProductMaster2ProductDTOConverter.convert(productMasterList);
        for (ProductDTO productDTO : productDTOList){
            ProductDistrict productDistrict = productDistrictRepository.
                    findByProductIdAndSchoolNo(productDTO.getProductId() , schoolNo);
            if (productDistrict!=null){
                List<ProductDistrict> result = new ArrayList<>();
                result.add(productDistrict);
                productDTO.setProductDistrictList(result);
            }

        }
        return productDTOList;
    }


    @Override
    public ProductDTO save(ProductDTO productDTO) {

        for (ProductDistrict productDistrict : productDTO.getProductDistrictList()){
            ProductDistrict result =  productDistrictRepository.save(productDistrict);
            if (result==null){
                throw new SellException(ResultEnum.PRODUCT_DISTRICT_NOT_EXIST);
            }
        }
        ProductMaster productMaster = new ProductMaster();
        BeanUtils.copyProperties(productDTO,productMaster);
        productMasterRepository.save(productMaster);
        return productDTO;
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList , String schoolNo) {
        for (CartDTO cartDTO: cartDTOList) {
            ProductDistrict productDistrict = productDistrictRepository.
                    findByProductIdAndSchoolNo(cartDTO.getProductId(),schoolNo);
            if (productDistrict == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productDistrict.getProductStock() + cartDTO.getProductQuantity();
            productDistrict.setProductStock(result);

            productDistrictRepository.save(productDistrict);
        }

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList , String schoolNo) {
        for (CartDTO cartDTO: cartDTOList) {
            ProductDistrict productDistrict = productDistrictRepository.
                    findByProductIdAndSchoolNo(cartDTO.getProductId(),schoolNo);
            if (productDistrict == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productDistrict.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productDistrict.setProductStock(result);

            productDistrictRepository.save(productDistrict);
        }
    }

    @Override
    public ProductDTO onSale(String productId, String schoolNo) {
        ProductDistrict productDistrict = productDistrictRepository.
                findByProductIdAndSchoolNo(productId,schoolNo);
        if (productDistrict == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productDistrict.getProductStatusEnum() == ProductStatusEnum.UP) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productDistrict.setProductStatus(ProductStatusEnum.UP.getCode());
        productDistrictRepository.save(productDistrict);
        ProductDTO productDTO = new ProductDTO();
        List<ProductDistrict> result = new ArrayList<>();
        result.add(productDistrict);
        ProductMaster productMaster = productMasterRepository.findOne(productId);
        BeanUtils.copyProperties(productMaster,productDTO);
        productDTO.setProductDistrictList(result);
        return productDTO;
    }

    @Override
    public ProductDTO offSale(String productId, String schoolNo) {
        ProductDistrict productDistrict = productDistrictRepository.
                findByProductIdAndSchoolNo(productId,schoolNo);
        if (productDistrict == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productDistrict.getProductStatusEnum() == ProductStatusEnum.DOWN) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productDistrict.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productDistrictRepository.save(productDistrict);
        ProductDTO productDTO = new ProductDTO();
        List<ProductDistrict> result = new ArrayList<>();
        result.add(productDistrict);
        ProductMaster productMaster = productMasterRepository.findOne(productId);
        BeanUtils.copyProperties(productMaster,productDTO);
        productDTO.setProductDistrictList(result);
        return productDTO;
    }

}
