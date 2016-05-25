package com.demo.thread;

import java.util.Vector;

/**
 * Created by 程祥 on 16/3/31.
 * Function：
 */
public class CountThread implements Runnable {

    private Vector result;
    private int pivot;

    public CountThread(int pivot,Vector result) {
        this.pivot = pivot;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(500);
                System.out.println(++pivot);
                result.add(pivot);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
