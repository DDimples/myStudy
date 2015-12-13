package com.mystudy.web.controller;

import com.mystudy.web.model.Persion;
import com.mystudy.web.service.impl.InitValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by 程祥 on 15/11/30.
 * Function： 注解
 */
@Controller
public class AnnotationController {

    @Autowired
    private InitValueService initValueService;

    @ModelAttribute
    public Persion addAttribute(@RequestParam(required = false) String arg){
        System.out.println("arg:"+arg);
        Persion persion = new Persion();
        persion.setName("test" + new Date().getTime());
        persion.setAddress("北京朝阳区酒仙桥中路");
        persion.setAge(10);
        persion.setSex('M');
        return persion;
    }

    @RequestMapping(value = "/annotationTest")
    @ResponseBody
    public Object testAttribute(HttpServletRequest request,HttpServletResponse response, Model model,String arg,
                                @CookieValue(value = "name",required = false) String cookie ){
        System.out.println("tt");

        Cookie tc = new Cookie("name","chengxiang");
        response.addCookie(tc);
        Object cookies = request.getCookies();
        return arg+cookie;
    }


    @RequestMapping(value = "/validTest",produces = "text/html;charset=utf-8")
    @ResponseBody
    public Object validArgs(Model model){
        System.out.println(model.containsAttribute("persion"));
        return "model 魔豆";
    }


}
