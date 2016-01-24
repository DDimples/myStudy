package com.mystudy.web.controller.util;

import com.mystudy.web.model.Goods;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by 程祥 on 16/1/24.
 * Function：
 */
public class DataValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        //用于校验clazz是否是Goods或者Goods的子类
        return Goods.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Goods obj = (Goods)target;
        if(obj.getName()==null){
            errors.rejectValue("name","name不能为空！");
        }
        if(obj.getNum()==null||obj.getNum()<0){
            errors.rejectValue("num","数量不能小于0");
        }
    }
}
