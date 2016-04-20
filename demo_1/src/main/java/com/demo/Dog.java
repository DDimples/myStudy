package com.demo;

/**
 * Created by 程祥 on 16/4/19.
 * Function：
 */
public class Dog extends Animal{
    //在构造方法执行后初始化
    String name = "default";

    public Dog(int legs) {
       //super()必须在第一行~ 不然编译不通过~
        super(legs);
        System.out.println(name);
    }
}

class Animal {

    int legs = 0;

    public Animal(int legs) {
        this.legs = legs;
    }
}
