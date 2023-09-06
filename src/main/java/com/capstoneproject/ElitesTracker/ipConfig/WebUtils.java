package com.capstoneproject.ElitesTracker.ipConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.EMPTY_STRING;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.X_FORWARDED_FOR;
@Component
@Slf4j
public class WebUtils implements HandlerInterceptor {


//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String remoteAddr = request.getHeader(X_FORWARDED_FOR);
//        if (remoteAddr == null || EMPTY_STRING.equals(remoteAddr)) {
//            remoteAddr = request.getRemoteAddr();
//        }
//        this.clientIpAddress = remoteAddr;
//        return true;
//    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String remoteAddr = request.getHeader(X_FORWARDED_FOR);
//        if (remoteAddr == null || EMPTY_STRING.equals(remoteAddr)) {
//            remoteAddr = request.getRemoteAddr();
//        }
//        this.clientIpAddress = remoteAddr;
//    }
//
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String remoteAddr = request.getHeader(X_FORWARDED_FOR);
        if (remoteAddr == null || EMPTY_STRING.equals(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
    }
}
