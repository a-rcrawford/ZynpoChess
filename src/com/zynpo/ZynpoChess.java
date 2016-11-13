package com.zynpo;

public class ZynpoChess {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        for(int i = 0; i < args.length; ++i) {
            System.out.println(String.format("args[%d] = %s", i, args[i]));
        }
    }

}