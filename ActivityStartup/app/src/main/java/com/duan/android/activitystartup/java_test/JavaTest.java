package com.duan.android.activitystartup.java_test;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public final class JavaTest {
    public static void main(String[] args){
        subListTest(); // 将List 分成指定数量的 subList
        //relationalOperator();
    }

    /** 将List 分成指定数量的 subList */
    public static void subListTest(){

        List<String> mData;
        List<String> subData;

        // 初始化 mData  size = 24
        mData = new ArrayList<>();
        for (int i = 0; i < 24; i++){
            mData.add(String.valueOf(i));
        }
        System.out.println("--------------------------- mData.size(): " + mData.size());

        for (int i = 0; i < 24; i++){
            int count = i % 8;
            if (count == 0){
                System.out.println("i % 8 == 0 ----------------------- i: " + i);

                subData = mData.subList(i, i + 8); // 返回 0--7 共8个数据; 不改变原List(mData 不受影响)
                for (String subDatum : subData) {
                    System.out.println("subDatum："+ subDatum);
                }
                System.out.println("--------------------------- mData.size(): " + mData.size());
            }
        }



    }

    /**
     *  (==)、(!=) 比较的是对象的引用
     *  Output: false  true
     *  n1、n2  是两个不同的引用； 但是指向相同的值。
     */
    public static void relationalOperator(){

        Integer n1 = new Integer(47);
        Integer n2 = new Integer(47);
        System.out.println("n1 == n2: " + (n1==n2));
        System.out.println("n1.equals(n2): " + (n1.equals(n2)));
    }
}
