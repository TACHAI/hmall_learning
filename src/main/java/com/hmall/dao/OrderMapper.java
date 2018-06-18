package com.hmall.dao;

import com.hmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);

    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectAllOrder();

//    二期新增定时关单
    List<Order> sselectOrderStatusByCreateTime(@Param("status")Integer status,@Param("date")String date);

    int closeOrderByOrderId(Integer id);
}