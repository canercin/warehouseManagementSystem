package com.warehouse.app.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BatchInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("POST")){
            System.out.println("POST method invoked for batch, new batch being creating.");
            // TODO data validation
        } else if (request.getMethod().equals("PUT")){
            System.out.println("PUT method invoked for batch, batch being fully updating.");
            // TODO data validation
        } else if (request.getMethod().equals("DELETE")){
            System.out.println("DELETE method invoked for batch, batch being deleting.");
        } else if (request.getMethod().equals("PATCH")) {
            System.out.println("PATCH method invoked for batch, batch being partially updating.");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
