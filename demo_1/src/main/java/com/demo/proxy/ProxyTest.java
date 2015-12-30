package com.demo.proxy;

import com.demo.proxy.impl.ProxySubject;
import com.demo.proxy.impl.TestServiceImpl;

import java.lang.reflect.Proxy;

/**
 * Created by 程祥 on 15/12/13.
 * Function：
 */
public class ProxyTest {

    public static void main(String[] args){
        TestService service = new TestServiceImpl();
        ProxySubject invoke = new ProxySubject(service);
        TestService proxy = (TestService)invoke.getProxyBean();
        proxy.printText();
    }

}
