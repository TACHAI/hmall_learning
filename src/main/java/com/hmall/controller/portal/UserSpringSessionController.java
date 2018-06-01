package com.hmall.controller.portal;

import com.hmall.common.Const;
import com.hmall.common.ResponseCode;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;
import com.hmall.service.IUserService;
import com.hmall.util.CookieUtil;
import com.hmall.util.JsonUtil;
import com.hmall.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by asus30 on 2018/6/1.
 */
@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {
    @Autowired
    private IUserService iUserService;
    /**
     *用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServiceResponse<User> response=iUserService.login(username,password);
        if(response.isSuccess()){

//01            session.setAttribute(Const.CURRENT_USER,response.getData());
// 02           CookieUtil.writeLoginToken(httpServletResponse,session.getId());
// 02           RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }
    return response;
    }
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> logout( HttpServletResponse httpServletResponse, HttpServletRequest request){
//01        session.removeAttribute(Const.CURRENT_USER);
//02        String loginToken = CookieUtil.readLoginToken(request);
//02        RedisPoolUtil.del(loginToken);
//02       CookieUtil.delLoginToken(httpServletResponse,request);
        return ServiceResponse.createBySuccess();
    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpSession session,HttpServletRequest httpServletRequest){
//        User user= (User) session.getAttribute(Const.CURRENT_USER);

//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        }
//        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }



    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> get_information(HttpSession session){
        User currentuser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentuser==null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录status=10");
        }
        return iUserService.getInformation(currentuser.getId());
    }
}
