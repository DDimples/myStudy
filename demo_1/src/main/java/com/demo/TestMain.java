package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.proxy.TestService;
import com.demo.proxy.impl.TestServiceImpl;

import java.io.*;
import java.util.*;


/**
 * Created by 程祥 on 16/1/23.
 * 思路 ：第一个数字最大的往前排
 */
public class TestMain {

    public static void main(String[] args){
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis()/1000L);


        String[] arr = new String[]{"3","30","34","5","9"};
        System.out.println(arr[1]);

    }

    public static String getBigestNum(String[] array) {
        if (array == null || array.length < 1) {
            throw new IllegalArgumentException("参数不许为空");
        }
        StringComparator comparator = new StringComparator();
        quickSort(array, 0, array.length - 1, comparator);
        StringBuilder builder = new StringBuilder(256);
        for (String s : array) {
            builder.append(s);
        }
        return builder.toString();
    }

    private static void quickSort(String[] array, int start, int end, Comparator<String> comparator) {
        if (start < end) {
            String pivot = array[start];
            int left = start;
            int right = end;
            while (start < end) {
                while (start < end && comparator.compare(array[end], pivot) >= 0) {
                    end--;
                }
                array[start] = array[end];
                while (start < end && comparator.compare(array[start], pivot) <= 0) {
                    start++;
                }
                array[end] = array[start];
            }
            array[start] = pivot;
            quickSort(array, left, start - 1, comparator);
            quickSort(array, start + 1, right, comparator);
        }
    }


    private static class StringComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            if (o1 == null || o2 == null) {
                throw new IllegalArgumentException("参数不能为空~");
            }
            String s1 = o1 + o2;
            String s2 = o2 + o1;
            return s2.compareTo(s1);
        }
    }

}


