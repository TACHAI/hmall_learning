package com.hmall.dao;

import com.hmall.pojo.Category;
import com.hmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    void batchInsert(List<OrderItem> orderItemList);

    List<OrderItem> getByOrderNoUserId(Long orderNo, Integer userId);

    List<OrderItem> getByOrderNo(Long orderNo);
}