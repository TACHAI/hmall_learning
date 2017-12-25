package com.hmall.service.impl;

import com.google.common.collect.Maps;
import com.hmall.common.ServiceResponse;
import com.hmall.dao.OrderMapper;
import com.hmall.pojo.Order;
import com.hmall.service.IOrderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by @Author tachai on 2017/10/10.
 *
 * @Email 1206966083@qq.com
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderservice {
    @Autowired
    private OrderMapper orderMapper;
    public ServiceResponse pay(Long orderNo,Integer userId,String path){
        Map<String ,String> resultMap= Maps.newHashMap();
        Order order=orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order==null){
            return ServiceResponse.createByErrorMessage("该用户没有订单");
        }
        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));
        return null;
    }
}
