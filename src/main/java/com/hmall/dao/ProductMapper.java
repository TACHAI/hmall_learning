package com.hmall.dao;

import com.hmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    List<Product> selectList();
    List<Product> selectByNameAndProductId(@Param("productName")String productName,@Param("productId")Integer productId);
    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName,@Param("categoryIdList")List<Integer> categoryIdList);
    //这里使用Integer,因为int无法为null,考虑到很多商品已经删除的情况
    Integer selectStockByProductId(Integer id);
}