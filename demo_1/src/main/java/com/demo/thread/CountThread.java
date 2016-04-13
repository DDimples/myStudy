package com.demo.thread;

/**
 * Created by 程祥 on 16/3/31.
 * Function：
 */
public class CountThread implements Runnable {

    private int pivot;

    public CountThread(int pivot) {
        this.pivot = pivot;
    }

    @Override
    public void run() {
        System.out.println(++pivot);
    }
}
