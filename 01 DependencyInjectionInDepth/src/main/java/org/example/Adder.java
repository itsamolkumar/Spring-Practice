package org.example;

public class Adder {
    private int a;
    private int b;
    Adder(int a, int b){
        this.a=a;
        this.b=b;
    }
    public void print(){
        System.out.println("sum of both number is:- "+(a+b));
    }
}
