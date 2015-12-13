package com.demo.proxy;

import com.demo.proxy.impl.InvokeImpl;
import com.demo.proxy.impl.TestServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 程祥 on 15/12/13.
 * Function：
 */
public class ProxyTest {

    public static void main(String[] args){
        TestService service = new TestServiceImpl();
        InvokeImpl invoke = new InvokeImpl(service);
        TestService proxy = (TestService)Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
                service.getClass().getInterfaces(),invoke);
        proxy.printText();


    }

}
