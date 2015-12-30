package com.demo.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by 程祥 on 15/12/30.
 * Function：反射学习测试
 */
public class ReflectTest {


    public static void main(String[] args){
        try {
            Class reflectClass = Class.forName("com.demo.proxy.ReflectModel");
            //此处相当于 new ReflectModel();
            ReflectModel reflectObj = (ReflectModel)reflectClass.newInstance();
            //可以正常输出
            reflectObj.voidMethod("reflectObj");

            Constructor constructor = reflectClass.getConstructor();
            ReflectModel constructorObj = (ReflectModel)constructor.newInstance();
            //可以正常输出
            reflectObj.voidMethod("constructorObj");

            Method voidMethod = reflectClass.getMethod("voidMethod",String.class);
            voidMethod.invoke(constructorObj, "methodInvoke");
            Method arrMthod = reflectClass.getMethod("setArr",String[].class);
            String[] arrStr = {"aaa","bb"};
            //arrStr前面需要加Object转型，
            //不然会报错java.lang.IllegalArgumentException:Wrong number of arguments
            arrMthod.invoke(constructorObj, (Object) arrStr);
            System.out.println(Arrays.toString(constructorObj.getArr()));

            Method setNum = reflectClass.getMethod("setNum",int.class);
            setNum.invoke(constructorObj,100);
            //都需要考虑访问性 private/protect/public
            Field numField = reflectClass.getField("num");
            System.out.println(numField.getInt(constructorObj));

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
