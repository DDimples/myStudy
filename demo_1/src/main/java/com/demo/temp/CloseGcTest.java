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
        //当把实现了AutoCloseable 的声明放在try中时，在try执行完前，对象的close方法会被调用
        try(CloseGcTest temp =new CloseGcTest()) {

//            temp = null;
//            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
