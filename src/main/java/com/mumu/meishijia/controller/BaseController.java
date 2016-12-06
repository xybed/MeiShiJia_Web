package com.mumu.meishijia.controller;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by 7mu on 2016/11/23.
 */
public class BaseController {
    protected String getApplicationPath(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        return servletContext.getRealPath("/");
    }
}
