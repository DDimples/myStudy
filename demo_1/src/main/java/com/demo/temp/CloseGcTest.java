package com.demo.temp;

/**
 * Created by 程祥 on 16/4/20.
 * Function：学习gc回收机制
 */
public class CloseGcTest implements AutoCloseable{

    public CloseGcTest() {
        System.out.println("CloseGcTest 我被创建了~");
    }

    @Override
    public void close() throws Exception {
        System.out.println("CloseGcTest 我被回收了！");
    }


    public static void main(String[] args){
        CloseGcTest temp =new CloseGcTest();
        try {
            temp = null;
            Thread.sleep(10000);
        }catch (Exception e){
            System.out.println("exception");
        }
    }

}
