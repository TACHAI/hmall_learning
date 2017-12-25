package com.hmall.controller.portal;

import com.hmall.common.Const;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by @Author tachai on 2017/10/10.
 *
 * @Email 1206966083@qq.com
 */
@Controller
@RequestMapping("/order/")
public class OrderController {
    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);

   // @Autowired
    //private IOrderService iOrderService;
    public ServiceResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGGIN.getCode(),ResponseCode.NEED_LOGGIN.getDesc());
        }
        String path=request.getSession().getServletContext().getRealPath("upload");
        return null;
    }
}
