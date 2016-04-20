package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mystudy.web.common.InitSequenceBean;
import com.mystudy.web.model.Goods;
import com.mystudy.web.model.Persion;
import com.mystudy.web.service.impl.InitValueService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    @RequestMapping(value = "/validTest")
    @ResponseBody
    public Object validArgs(Model model,String t){
        System.out.println(model.containsAttribute("persion"));
        return "model 魔豆"+t;
    }


    public void test(){
        initSequenceBean.initMethodByXml();

        System.out.println("test~"+initSequenceBean.getTestInitValue());
    }

    @Qualifier("initTestBean_1")
    @Autowired
    private InitSequenceBean initSequenceBean;


    @RequestMapping(value = "/requestBodyTest",method = RequestMethod.POST)
    @ResponseBody
    public Object requestBodyTest(@RequestBody Goods goods){

        return goods==null?"null":goods.toString();
    }

    @RequestMapping(value = "/atest")
    @ResponseBody
    public Object testBody(){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/requestBodyTest");
        httpPost.setHeader("content-type", "application/json");
        Goods goods = new Goods();
        goods.setName("apple");
        goods.setNum(100.00);
        goods.setId("00122");
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(goods),"UTF-8");
        httpPost.setEntity(stringEntity);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {

                    return EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }



    @RequestMapping(value = "/jsonpTest")
    @ResponseBody
    public Object jsonpTest(HttpServletRequest request){
        try {

            String callBack = request.getParameter("jsonpCallback");
            //TODO 添加对象
            return callBack+"("+JSON.toJSONString(new Object())+")";
//            return new JSONPObject("callback", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONPObject("callbackTest","重启失败~");
    }

}
