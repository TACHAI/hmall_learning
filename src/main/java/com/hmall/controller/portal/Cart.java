package com.hmall.controller.portal;

import com.hmall.common.Const;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;
import com.hmall.service.ICartService;
import com.hmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by @Author tachai on 2017/9/2.
 *
 * @Email 1206966083@qq.com
 */
@Controller
@RequestMapping("/cart/")
public class Cart {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse<CartVo> list(HttpSession session, Integer productId, Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }


    @RequestMapping("add.do")
    @ResponseBody
    public ServiceResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
return iCartService.add(user.getId(),productId,count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServiceResponse<CartVo> udate(HttpSession session, Integer productId, Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
      return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServiceResponse<CartVo> deleteProduct(HttpSession session, String productIds){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.deleteProdut(user.getId(),productIds);
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServiceResponse<CartVo> selectAll(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }
    //全选
    //全反选
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServiceResponse<CartVo> unSelectAll(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
    //单独反选
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServiceResponse<CartVo> unSelect(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }
    //单独选
    @RequestMapping("select.do")
    @ResponseBody
    public ServiceResponse<CartVo> select(HttpSession session,Integer productId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    //查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么就是10
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServiceResponse<Integer> getCartProductCount(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createBySuccess(0);
        }

        return iCartService.getCartProductCount(user.getId());
    }

}
