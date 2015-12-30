package com.demo.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 程祥 on 15/12/13.
 * Function：
 */
public class ProxySubject implements InvocationHandler {

    //被代理对象
    private Object object;

    public ProxySubject(Object object) {
        this.object = object;
    }

    public Object getProxyBean(){
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(), this);
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理增强
        System.out.println("InvokeImpl proxy add function~");
        Object result = method.invoke(object, args);
        System.out.println(proxy.getClass().getName());
        //。。。some other function
        return result;
    }
}
