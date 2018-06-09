package com.hmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;

/**
 * Created by @Author tachai
 * date 2018/6/9 19:59
 *
 * @Email 1206966083@qq.com
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{} Exception",httpServletRequest.getRequestURI(),e);
        ModelAndView modelAndVie=new ModelAndView(new MappingJackson2JsonView());
//        当使用jackson2.x的时候使用MappingJackson2JsonView,课程中使用的是1.9
        modelAndVie.addObject("status",ResponseCode.ERROR.getCode());
        modelAndVie.addObject("msg","接口异常详情查看服务端异常信息");
        modelAndVie.addObject("data",e.toString());
        return modelAndVie;
    }
}
