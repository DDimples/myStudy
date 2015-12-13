package com.demo.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by 程祥 on 15/12/13.
 * Function：
 */
public class InvokeImpl implements InvocationHandler {

    //被代理对象
    private Object object;

    public InvokeImpl(Object object) {
        this.object = object;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //代理增强
        System.out.println("InvokeImpl proxy add function~");
        System.out.println(proxy.getClass().getName());
        return method.invoke(object,args);
    }
}
