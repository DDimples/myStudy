package com.demo;

/**
 * Created by 程祥 on 16/1/25.
 * Function：
 */
public class ClassB extends ClassA{
    public ClassB(){
        System.out.println("Hello B");
    }
    {
        System.out.println("I'm class B");
    }

    static {
        System.out.println("static B");
    }

    public void test(int a){
        System.out.println(a);
    }
    public void test(String a){
        System.out.println(a);
    }

    public static void main(String[] args){

//        System.out.println(ClassB.num);
//        ClassB bb = new ClassB();
//        System.out.println(bb.getClass().getClassLoader().toString());
//        System.out.println(bb.getClass().getClassLoader().getParent().toString());
//        char ch = new Character('a');
//        bb.test(ch);
//        int a=10,b=4,c=20,d=6;
//        int e = (a++*b+c*--d);
//        System.out.println(e);
        new ClassB();
//        new ClassB();
    }

}

class ClassA{
    static int num = 100;
    public ClassA(){
        System.out.println("Hello A");
    }
    {
        System.out.println("I'm class A");
    }

    static {
        System.out.println("static A");
    }
}
