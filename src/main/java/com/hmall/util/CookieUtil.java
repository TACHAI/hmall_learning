package com.hmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @Author tachai
 * date 2018/5/27 22:11
 *
 * @Email 1206966083@qq.com
 */@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMIN=".laishishui.com";
    private final static String COOKIE_NAME="hmall_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks=request.getCookies();
        if(cks != null){
            for(Cookie ck: cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck= new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMIN);
        ck.setHttpOnly(true);//防止页面脚本读取cookie
        ck.setPath("/");//设置在根目录
        //单位是秒
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        ck.setMaxAge(60*60*24*365);//如果是-1，代表是永久
        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }
    //删除cookie
    public static void delLoginToken(HttpServletResponse response,HttpServletRequest request){
        Cookie[] cks =request.getCookies();
        if(cks !=null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置成0，代表删除此cookie
                    log.info("del cookieName:{}, cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
