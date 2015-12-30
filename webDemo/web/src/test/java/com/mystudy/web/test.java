package com.mystudy.web;

import com.mystudy.web.controller.AnnotationController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 程祥 on 15/12/24.
 * Function：
 */
public class Test {

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-context.xml");
        AnnotationController t = (AnnotationController) context.getBean("annotationController");
        t.test();


    }

}
