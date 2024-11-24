package com.warehouse.app.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ProductInterceptor implements HandlerInterceptor {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("POST")){
            LOG.info("POST method invoked for product, new product being creating.");
            // TODO data validation
        } else if (request.getMethod().equals("PUT")){
            LOG.info("PUT method invoked for product, product being fully updating.");
            // TODO data validation
        } else if (request.getMethod().equals("DELETE")){
            LOG.info("DELETE method invoked for product, product being deleting.");
        } else if (request.getMethod().equals("PATCH")) {
            LOG.info("PATCH method invoked for product, product being partially updating.");
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
