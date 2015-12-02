package com.mystudy.web.controller;

import com.mystudy.web.model.Persion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 程祥 on 15/11/30.
 * Function： 注解
 */
@Controller
public class AnnotationController {



    @ModelAttribute
    public Persion addAttribute(@RequestParam String arg){
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
    public Object testAttribute(HttpServletRequest request, Model model,String arg,
                                @CookieValue(value = "JSESSIONID",required = false) String cookie ){
        System.out.println("tt");
        return arg+cookie;
    }


}
