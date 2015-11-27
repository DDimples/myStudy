package com.mystudy.web.controller;

import com.mystudy.web.common.BaseController;
import com.mystudy.web.common.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 程祥 on 15/11/26.
 * Function：
 */

@Controller
public class IndexController extends BaseController {
    private final Logger logger = LogUtil.getCommonLogger();
    private  final Logger testLogger = LogUtil.getTestLogger();
    @RequestMapping(value = "/")
    public ModelAndView index(){
        logger.debug("index ********");
        return new ModelAndView("index.html");
    }

    @RequestMapping(value = "/index")
    public ModelAndView index2(){
        logger.debug("index2 ********");
        return new ModelAndView("index.html");
    }

    @RequestMapping(value = "/test",produces = "text/html; charset=utf-8")
    @ResponseBody
    public Object index3(){
        logger.debug("你好 ********");
        testLogger.debug("你好~~~~*****");
        return "hello 你好";
    }

}
