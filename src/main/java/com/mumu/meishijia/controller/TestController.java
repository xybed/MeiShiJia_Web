package com.mumu.meishijia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试用的controller
 * Created by Administrator on 2017/3/28.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController{
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return getApplicationPath();
    }
}
