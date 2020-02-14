package com.delpoy;

/**
 * @Author lyf
 * @Date 21:21 2020/2/3
 */
public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.getPath());
    }

    public String getPath(){
        return this.getClass().getResource("/deployContent.properties").getPath();
    }

}
