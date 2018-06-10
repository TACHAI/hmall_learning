package com.hmall.common.intereceptor;

import com.alipay.api.internal.util.StringUtils;
import com.google.common.collect.Maps;
import com.hmall.common.Const;
import com.hmall.common.ServiceResponse;
import com.hmall.pojo.User;
import com.hmall.util.CookieUtil;
import com.hmall.util.JsonUtil;
import com.hmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by @Author tachai
 * date 2018/6/10 21:46
 *
 * @Email 1206966083@qq.com
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        //请求Controller中的方法名
        HandlerMethod handlerMethod=(HandlerMethod)o;
        //解析HandlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key以及value是什么，我们打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String) entry.getKey();
            String mapValue= "";
            //request这个参数的map,里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof String []){
               String[] strs=(String[])obj;
               mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        if("UserManageController".equals(className)&&"login".equals(methodName)){
            log.info("权限拦截器拦截掉请求，className:{},methodName:{}",className,methodName);
            //如果是拦截到登录，不打印参数
            return true;
        }
        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(!StringUtils.isEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2obj(userJsonStr,User.class);
        }
        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false.即不会调用controller里的方法
            httpServletResponse.reset();//这里要添加reset，否者报异常getWriter()
            httpServletResponse.setCharacterEncoding("UTF-8");//设置编码，否者会乱码
            httpServletResponse.setContentType("application/json;charset=UTF-8");//设置返回值的类型

            PrintWriter out = httpServletResponse.getWriter();
            if(user == null){
                out.print(JsonUtil.obj2String(ServiceResponse.createByErrorMessage("拦截器拦截，用户未登录")));
            }else {
                out.print(JsonUtil.obj2String(ServiceResponse.createByErrorMessage("拦截器拦截，用户无权限炒作")));

            }
            if(user == null){
                if("ProductManageController".equals(className)||"".equals(methodName)){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(ServiceResponse.createByErrorMessage("拦截器拦截，用户无权限炒作"));
                }
            }else {
                if("ProductManageController".equals(className)||"".equals(methodName)){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(ServiceResponse.createByErrorMessage("拦截器拦截，用户无权限炒作"));
                }
            }
            out.flush();
            out.close();
            return false;
        }
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
