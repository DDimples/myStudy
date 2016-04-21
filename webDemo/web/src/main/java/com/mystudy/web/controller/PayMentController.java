package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mystudy.web.cache.CacheTool;
import com.mystudy.web.common.util.HttpClientUtils;
import com.mystudy.web.common.util.HttpUtil;
import com.mystudy.web.common.util.StringUtil;
import com.mystudy.web.controller.util.DataValidator;
import com.mystudy.web.model.Goods;
import com.mystudy.web.model.OrderDetailEntity;
import com.mystudy.web.model.PaymentDTO;
import com.mystudy.web.model.RefundModel;
import net.sf.ehcache.CacheManager;
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
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
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
import java.util.*;

/**
 * Created by 程祥 on 16/1/12.
 * Function：
 */
@Controller
@RequestMapping(value = "/pay")
public class PayMentController {

//    private String cacheKey = "2016-03-22 cacheTest";

    @RequestMapping(value = "/addCache")
    @ResponseBody
    public Object addCache(String obj,String cacheKey){
        CacheTool.setObject("myStudy","test",cacheKey,obj);

        String cacheVaule = (String)CacheTool.getObject("myStudy","test",cacheKey);
        cacheVaule = obj;

        if(StringUtil.isEmpty(cacheVaule)){
            cacheVaule = "不存在缓存值~";
        }
        return cacheVaule;
    }

    @RequestMapping(value = "/getCache")
    @ResponseBody
    public Object getCache(String cacheKey){
        Object cacheValue = CacheTool.getObject("myStudy", "test", cacheKey);
        return cacheValue;
    }

    @RequestMapping(value = "/delCache")
    @ResponseBody
    public Object delCache(String obj,String cacheKey){
        CacheTool.setObject("myStudy","test",cacheKey,null);
        return "清除缓存";
    }

    @RequestMapping(value = "/showAllCacheKeys")
    @ResponseBody
    public Object showAllCaches(){
        HashMap<String, List> caches = new HashMap<String,List>();
        CacheManager manager = CacheManager.create();
        String[] cacheNames = manager.getCacheNames();
        for(String name : cacheNames) {
            List<String> keys = new ArrayList<String>();
            keys = manager.getCache(name).getKeys();
            caches.put(name, keys);
        }
        return JSON.toJSONString(caches);
    }


    @RequestMapping(value = "/payTest")
    @ResponseBody
    public Object test(){

        HttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("http://newpayment-api-qry.vip.elong.com:8082/payment/token.html?business_type=1026&merchant_id=190010023&out_trade_no=123456");


        HttpPost httpPost = new HttpPost("http://mhuodong.elong.com/WxCrowdfunding/payNotifyAction?orderId=5&check=A89116AD8DDB0E085EF36113CB91AFBB");
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
            String temp = "{\"notifyUrl\":\"http://mhuodong.elong.com/WxCrowdfunding/payNotifyAction?orderId=11&check=1199D258A1C9BDCE01EA0682D8D763E8\",\"callBackRequest\":{\"trade_no\":100000000050041343,\"pay_time\":\"2016-01-30 15:50:43\",\"result_status\":1,\"error_info\":\"\",\"sales_verify\":0,\"pay_type_info_list\":[{\"operator\":\"\",\"currency\":4601,\"trans_type\":3003,\"bank_name\":\"微信支付\",\"customer_service_amt\":0.0,\"pay_amt\":5.0}]},\"trade_no\":100000000050041343,\"pay_time\":\"2016-01-30 15:50:43\",\"result_status\":1,\"error_info\":\"\",\"sales_verify\":0,\"pay_type_info_list\":[{\"operator\":\"\",\"currency\":4601,\"trans_type\":3003,\"bank_name\":\"微信支付\",\"customer_service_amt\":0.0,\"pay_amt\":5.0}]}";
            StringEntity stringEntity = new StringEntity(temp,"UTF-8");
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
        System.out.println("say something~" + body);
        return "success";
    }

    @RequestMapping(value = "/testValidator")
    @ResponseBody
    public Object testValidator(){
        Goods goods = new Goods();
        DirectFieldBindingResult fieldBindingResult =
                new DirectFieldBindingResult(goods,Goods.class.getName());
        ValidationUtils.invokeValidator(new DataValidator(), goods, fieldBindingResult);

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

    @RequestMapping(value = "/testValidator2")
    @ResponseBody
    public Object testValidator2(){
        Map<String,String> params = new HashMap<>();
        params.put("req","{modelData:{\"orderId\":\"190000000033095660\",\"orderStatus\":\"出票失败\",\"trainNO\":\"G12 北京-上海\",\"departureTime\":\"2016-02-04 10:20\",\"seatInfo\":\"二等座 D8、D9\",\"passenger\":\"小二黑、小三\",\"amount\":100,\"currency\":\"$\",\"refundMessage\":\"7个工作日内退回原支付账户。\",\"toCityName\":\"上海\",\"toCityPinyin\":\"beijing\",\"elongCardNo\":\"190000000033095660\",\"openId\":\"oE_FowL1XY7N543_qRkizkCjEi7M\"},\"vehicleType\":\"TRAIN_TICKET_FAIL\"}");
        HttpPost post = postForm("http://192.168.9.19:8319/template/sendTemplateMessage",params);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse response = client.execute(post);
            System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "success";
    }

    private String url ="http://192.168.9.19:8318/elong/weixinmp/msg/getAccessTokenForApi";

    @RequestMapping(value = "/testHttpClientPool")
    @ResponseBody
    public Object testHttpClientPool(){
        StopWatch sw = new StopWatch();
        sw.start();
        HttpClientUtils utils = new HttpClientUtils();
        for (int i=0;i<20;i++){
            HttpClientUtils.HttpResult result = utils.get(url, "");
            System.out.println(i+"次请求结果为：-- "+result.getStatusCode());
        }
        sw.stop();

        return sw.getLastTaskTimeMillis();
    }

    @RequestMapping(value = "/testHttpClientPool2")
    @ResponseBody
    public Object testHttpClientPool2(){

        StopWatch sw = new StopWatch();
        sw.start();

        HttpUtil utils = new HttpUtil();
        for (int i=0;i<20;i++){
//            HttpGet httpGet = new HttpGet(url);
            HttpResponse result = null;
            try {
                result = utils.get(url);
                EntityUtils.consume(result.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(i+"次请求结果为：-- "+(result.getStatusLine()==null?"error":result.getStatusLine().getStatusCode()));
        }
        sw.stop();

        return sw.getLastTaskTimeMillis();
    }

    @RequestMapping(value = "/testHttpClientPool3")
    @ResponseBody
    public Object testHttpClientPool3(){

        StopWatch sw = new StopWatch();
        sw.start();
        HttpClient utils = HttpClientBuilder.create().build();
        for (int i=0;i<20;i++){
            HttpGet httpGet = new HttpGet(url);

            HttpResponse result = null;
            try {
                result = utils.execute(httpGet);
                EntityUtils.consume(result.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(i+"次请求结果为：-- "+(result.getStatusLine()==null?"error":result.getStatusLine().getStatusCode()));
        }
        sw.stop();

        return sw.getLastTaskTimeMillis();
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
