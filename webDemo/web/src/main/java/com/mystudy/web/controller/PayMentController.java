package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mystudy.web.controller.util.DataValidator;
import com.mystudy.web.model.Goods;
import com.mystudy.web.model.OrderDetailEntity;
import com.mystudy.web.model.PaymentDTO;
import com.mystudy.web.model.RefundModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.provider.MD5;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 程祥 on 16/1/12.
 * Function：
 */
@Controller
public class PayMentController {

    @RequestMapping(value = "/payTest",produces = "text/html; charset=utf-8")
    @ResponseBody
    public Object test(){

        HttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("http://newpayment-api-qry.vip.elong.com:8082/payment/token.html?business_type=1026&merchant_id=190010023&out_trade_no=123456");


        HttpPost httpPost = new HttpPost("http://newpayment-api-cc.vip.elong.com:8081/payment/refund.html");
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("req",JSON.toJSONString(req)));
        String sign = "amt=1.00&business_type=1026&merchant_id=190010023&notify_url=http://mhuodong.elong.com/WxCrowdfunding/refundNotifyAction&order_id=1&sign_type=MD5&trade_no=5000018097&key=698F62387BC158359F9BDF409D821AEF";
        String md5 = DigestUtils.md5Hex(sign);
        RefundModel model = new RefundModel();
        model.setAmt(1.00);
        model.setBusiness_type(1026);
        model.setMerchant_id("190010023");
        model.setNotify_url("http://mhuodong.elong.com/WxCrowdfunding/refundNotifyAction");
        model.setOrder_id("1");
        model.setSign(md5.toUpperCase());
        model.setSign_type("MD5");
        model.setTrade_no("5000018097");
        String json = JSON.toJSONString(model);

        try {
            StringEntity stringEntity = new StringEntity(json,"UTF-8");
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject uploadJson = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
//                    System.out.println(uploadJson.get("payUrl"));？
                    return uploadJson.toJSONString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    @RequestMapping(value = "/testRaw",method = RequestMethod.POST)
    @ResponseBody
    public Object testRaw(@RequestBody String body){
        System.out.println("say something~"+body);
        return "success";
    }

    @RequestMapping(value = "/testValidator")
    @ResponseBody
    public Object testValidator(){
        Goods goods = new Goods();
        DirectFieldBindingResult fieldBindingResult =
                new DirectFieldBindingResult(goods,Goods.class.getName());
        ValidationUtils.invokeValidator(new DataValidator(),goods,fieldBindingResult);

        if(fieldBindingResult.hasErrors()){
            List<FieldError> errors = fieldBindingResult.getFieldErrors();
            JSONObject json = new JSONObject();
            for (FieldError error : errors) {
                json.put(error.getField(), error.getCode());
            }
            return JSON.toJSONString(json);
        }
        return "success";
    }



    private HttpPost postForm(String url, Map<String, String> params){

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        Set<String> keySet = params.keySet();
        for(String key : keySet) {
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpPost;
    }
}
