package com.hmall.service;

import com.github.pagehelper.PageInfo;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.Product;

/**
 * Created by @Author tachai on 2017/8/28.
 *
 * @Email 1206966083@qq.com
 */
public interface IProductService {
    ServiceResponse saveOrUpdateProduct(Product product);
    ServiceResponse<String> setSaleStatus(Integer productId,Integer status);
    ServiceResponse<Object> manageProductDetail(Integer productId);
    ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServiceResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
}
