package com.hmall.service;

import com.github.pagehelper.PageInfo;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.Shipping;

/**
 * Created by @Author tachai on 2017/9/10.
 *
 * @Email 1206966083@qq.com
 */
public interface IShippingServiceimpl {
    ServiceResponse add(Integer userId, Shipping shipping);
    ServiceResponse<String> del(Integer userId,Integer shippingId);
    ServiceResponse update(Integer userId,Shipping shipping);
    ServiceResponse<Shipping> select(Integer userId,Integer shippingId);
    ServiceResponse<PageInfo> list(Integer userId, int pagaeNum, int pageSize);
}
