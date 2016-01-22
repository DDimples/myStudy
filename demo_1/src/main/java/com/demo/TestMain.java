package com.demo;

import com.alibaba.fastjson.JSONObject;
import com.demo.model.GetRedPacketReq;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

/**
 * Created by 程祥 on 16/1/22.
 * Function：
 */
public class TestMain {

    public static void main(String[] args){
        System.out.println(DigestUtils.md5Hex("123"));

    }

}
