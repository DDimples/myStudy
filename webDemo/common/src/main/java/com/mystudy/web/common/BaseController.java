package com.mystudy.web.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by 程祥 on 15/11/27.
 * Function：Controller
 * 分页信息、公共数据、头尾、等等
 */
public abstract class BaseController {

    @ModelAttribute
    public void initController(Model model){
        model.addAttribute("baseName","baseName");
        model.addAttribute("title","公共头");
    }


}
