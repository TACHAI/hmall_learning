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

    public ServiceResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
return iCartService.add(user.getId(),productId,count);
    }
}
