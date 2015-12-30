package com.mystudy.web.service;

import java.util.concurrent.Future;

/**
 * Created by 程祥 on 15/11/29.
 * Function：
 */
public interface TestService {
    String testMethod(String name) throws Exception;

    Future<String> testMultithreading();
}
