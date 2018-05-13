package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.component.RedisLock;
import com.hnust.wxsell.dataobject.DispatchDetail;
import com.hnust.wxsell.dataobject.GroupMaster;
import com.hnust.wxsell.dataobject.GroupProduct;
import com.hnust.wxsell.dto.CartDTO;
import com.hnust.wxsell.dto.DispatchDTO;
import com.hnust.wxsell.dto.GroupMasterDTO;
import com.hnust.wxsell.dto.ProductDTO;
import com.hnust.wxsell.enums.ResultEnum;
import com.hnust.wxsell.exception.SellException;
import com.hnust.wxsell.repository.GroupProductRepository;
import com.hnust.wxsell.service.GroupMasterService;
import com.hnust.wxsell.service.GroupProductService;
import com.hnust.wxsell.service.ProductService;
import com.hnust.wxsell.utils.JsonUtil;
import com.hnust.wxsell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZZH
 * @date 2018/4/7 0007 16:26
 **/
@Service
public class GroupProductServiceImpl implements GroupProductService {
    private static final int TIMEOUT = 10 * 1000;    //超时时间十秒

    @Autowired
    private GroupProductRepository groupProductRepository;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private GroupMasterService groupMasterService;

    @Autowired
    private ProductService productService;

    @Override
    public List<GroupProduct> findBySchoolNoAndGroupNo(String schoolNo, String groupNo) {
        return groupProductRepository.findBySchoolNoAndGroupNo(schoolNo,groupNo);
    }

    @Override
    public GroupProduct findBySchoolNoAndGroupNoAndProductId(String schoolNo, String groupNo, String productId) {
        return groupProductRepository.findBySchoolNoAndGroupNoAndProductId(schoolNo,groupNo,productId);
    }

    @Override
    public GroupProduct save(GroupProduct groupProduct) {
        return groupProductRepository.save(groupProduct);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList, String groupNo, String schoolNo) {

        for (CartDTO cartDTO: cartDTOList) {
            GroupProduct groupProduct = groupProductRepository.findBySchoolNoAndGroupNoAndProductId(schoolNo,groupNo ,cartDTO.getProductId());
            if (groupProduct == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = groupProduct.getProductStock() + cartDTO.getProductQuantity();
            groupProduct.setProductStock(result);
            groupProductRepository.save(groupProduct);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList, String groupNo, String schoolNo) {
        for (CartDTO cartDTO: cartDTOList) {
            //加锁
            long time = System.currentTimeMillis() + TIMEOUT;
            if(!redisLock.lock(cartDTO.getProductId(),String.valueOf(time))){
                throw new SellException(ResultEnum.DECREASE_STOCK_ERROR);
            }

            GroupProduct groupProduct =  groupProductRepository.findBySchoolNoAndGroupNoAndProductId(schoolNo,groupNo,cartDTO.getProductId());
            if (groupProduct == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = groupProduct.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            groupProduct.setProductStock(result);

            groupProductRepository.save(groupProduct);

            //解锁
            redisLock.unlock(cartDTO.getProductId(), String.valueOf(time));
        }
    }

    /**
     * 寝室商品补货
     * @param dispatchDTO
     */
    @Override
    @Transactional
    public void replenish(DispatchDTO dispatchDTO) {
        //TODO
        //回收库存
   /*     GroupMasterDTO recycleProduct = groupMasterService.
                findByGroupNoAndSchoolNo(dispatchDTO.getGroupNo(),dispatchDTO.getSchoolNo());

        List<CartDTO> cartDTOList = recycleProduct.getGroupProductList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductStock())
        ).collect(Collectors.toList());

        productService.increaseStock(cartDTOList,dispatchDTO.getSchoolNo());

        //清空寝室所有商品
        groupProductRepository.
                removeAllBySchoolNoAndGroupNo(dispatchDTO.getSchoolNo(),dispatchDTO.getGroupNo());*/

      GroupMasterDTO groupMasterDTO = groupMasterService.
              findByGroupNoAndSchoolNo(dispatchDTO.getGroupNo(),dispatchDTO.getSchoolNo());
        if(groupMasterDTO == null){
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }
      boolean flag = true;
        //遍历补货商品
        for(DispatchDetail dispatchDetail : dispatchDTO.getDispatchDetailList()){
            for (GroupProduct groupProduct : groupMasterDTO.getGroupProductList()) {
                if (dispatchDetail.getProductId().equals(groupProduct.getProductId())) {
                    ProductDTO productDTO = productService.findOne(dispatchDetail.getProductId());
                    if (productDTO == null) {
                        throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
                    }

                    //补货车中商品入库
                    groupProduct.setProductStock(dispatchDetail.getProductQuantity()+groupProduct.getProductStock());
                    groupProduct.setProductQuantity(dispatchDetail.getProductQuantity()+groupProduct.getProductQuantity());
                    groupProductRepository.save(groupProduct);
                    flag = false;
                    break;
                }

            }
            if (flag) {
                ProductDTO productDTO = productService.findOne(dispatchDetail.getProductId());
                if (productDTO == null) {
                    throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
                }
                GroupProduct groupProduct = new GroupProduct();
                BeanUtils.copyProperties(productDTO, groupProduct);
                groupProduct.setProductStock(dispatchDetail.getProductQuantity());
                groupProduct.setProductQuantity(dispatchDetail.getProductQuantity());
                groupProduct.setId(KeyUtil.genUniqueKey());
                groupProduct.setSchoolNo(dispatchDTO.getSchoolNo());
                groupProduct.setGroupNo(dispatchDTO.getGroupNo());
                groupProductRepository.save(groupProduct);
            }
            flag = true;
        }

        //增加此次补货商品金额、商品信息快照==>入库入库
        GroupMasterDTO result = groupMasterService.
                findByGroupNoAndSchoolNo(dispatchDTO.getGroupNo(),dispatchDTO.getSchoolNo());
        if(result == null){
            throw new SellException(ResultEnum.GROUP_NOT_EXIST);
        }
        GroupMaster groupMaster = new GroupMaster();
        BeanUtils.copyProperties(result, groupMaster);

        String snapItems = JsonUtil.toJson(dispatchDTO.getDispatchDetailList());
        groupMaster.setGroupAmount(dispatchDTO.getDispatchAmount().add(groupMaster.getGroupAmount()));
        groupMaster.setSnapItems(snapItems);

        groupMasterService.save(groupMaster);
    }

    @Override
    public void removeAll(GroupProduct groupProduct) {

        groupProductRepository.delete(groupProduct.getId());

    }

    @Override
    @Transactional
    public void increaseSales(List<CartDTO> cartDTOList, String groupNo, String schoolNo) {
        for (CartDTO cartDTO: cartDTOList) {
            GroupProduct groupProduct = groupProductRepository.findBySchoolNoAndGroupNoAndProductId(schoolNo,groupNo ,cartDTO.getProductId());
            if (groupProduct == null) {
                //TODO
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = groupProduct.getProductSales() + cartDTO.getProductQuantity();
            groupProduct.setProductSales(result);
            groupProductRepository.save(groupProduct);
        }
    }

    @Override
    @Transactional
    public void decreaseSales(List<CartDTO> cartDTOList, String groupNo, String schoolNo) {

        for (CartDTO cartDTO: cartDTOList) {
            //加锁
            long time = System.currentTimeMillis() + TIMEOUT;
            if(!redisLock.lock(cartDTO.getProductId(),String.valueOf(time))){
                throw new SellException(ResultEnum.DECREASE_STOCK_ERROR);
            }

            GroupProduct groupProduct =  groupProductRepository.findBySchoolNoAndGroupNoAndProductId(schoolNo,groupNo,cartDTO.getProductId());
            if (groupProduct == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = groupProduct.getProductSales() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            groupProduct.setProductSales(result);

            groupProductRepository.save(groupProduct);

            //解锁
            redisLock.unlock(cartDTO.getProductId(), String.valueOf(time));
        }
    }

}
