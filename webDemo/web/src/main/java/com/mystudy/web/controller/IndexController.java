package com.mystudy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 程祥 on 15/11/26.
 * Function：
 */

@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public ModelAndView index(){
        return new ModelAndView("index.html");
    }

    @RequestMapping(value = "/index")
    public ModelAndView index2(){
        return new ModelAndView("index.html");
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public Object index3(){
        return "hello 你好";
    }

}
