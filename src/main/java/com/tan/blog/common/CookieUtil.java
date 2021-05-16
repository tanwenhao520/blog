package com.tan.blog.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CookieUtil {
    /**
     * 获取Cookie
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookie( HttpServletRequest request,String cookieName){
        if(request.getCookies()!=null){
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    /**
     * 设置Cookie
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue){
        HttpSession session = request.getSession();
        Cookie cookie = new Cookie(cookieName,System.currentTimeMillis()+"");
        cookie.setPath("/");
        cookie.setMaxAge(7 *  24 * 60 * 1000);
        cookie.setValue(cookieValue);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     * @param request
     * @param response
     * @param cookieName
     */
    public  static void delCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());
            if(cookie.getName().equals(cookieName) ){
               cookie.setMaxAge(0);
               cookie.setPath("/");
               response.addCookie(cookie);
            }
        }

    }


}
