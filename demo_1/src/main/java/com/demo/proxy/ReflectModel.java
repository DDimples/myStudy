package com.demo.proxy;

/**
 * Created by 程祥 on 15/12/30.
 * Function：学习反射用的model
 */
public class ReflectModel {

    private String string;
    public int num;
    private String[] arr;

    public void voidMethod(String arg){
        System.out.println("voidMethod********"+arg);
    }

    public String argMethod(String arg){
        System.out.println(arg);
        return arg+"******argMethod";
    }

    private void priMethod(){
        System.out.println("priMethod");
    }


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }


}
