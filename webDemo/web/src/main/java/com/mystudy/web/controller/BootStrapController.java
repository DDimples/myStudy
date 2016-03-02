package com.mystudy.web.controller;

import com.mystudy.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 程祥 on 16/1/29.
 * Function：
 */
@Controller
@RequestMapping(value = "/bootstrap")
public class BootStrapController extends BaseController{

    @RequestMapping(value = "/fixedNav")
    public ModelAndView flxedNavBar(){
        ModelAndView mv = new ModelAndView("bootstrap/fixedNavbar");

        return mv;
    }

    @RequestMapping(value = "/carousel")
    public ModelAndView carousel(){
        ModelAndView mv = new ModelAndView("bootstrap/carousel");

        return mv;
    }

    @RequestMapping(value = "/t_{name}")
    public ModelAndView bootstrap(@PathVariable String name){
        System.out.println(name);
        ModelAndView mv = new ModelAndView("bootstrap/"+name);
        return mv;
    }

}
