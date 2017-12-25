package com.hmall.service;

import com.hmall.common.ServiceResponse;
import com.hmall.vo.CartVo;

/**
 * Created by @Author tachai on 2017/9/2.
 *
 * @Email 1206966083@qq.com
 */
public interface ICartService {
    ServiceResponse<CartVo> add(Integer userId, Integer productId, Integer count);
    ServiceResponse<CartVo> update(Integer userId,Integer productId,Integer count);
    ServiceResponse<CartVo> deleteProdut(Integer userId,String productIds);
    ServiceResponse<CartVo> list(Integer userId);
    ServiceResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);
    ServiceResponse<Integer> getCartProductCount(Integer userId);
}
