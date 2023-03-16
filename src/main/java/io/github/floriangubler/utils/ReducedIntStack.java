package io.github.floriangubler.utils;

import java.util.EmptyStackException;

public class ReducedIntStack {
    private final int[] arr;
    private final int cap;
    private int top;

    public ReducedIntStack(int size) {
        cap = size;
        arr = new int[size];
        top = -1;
    }

    public void push(int n) {
        if (top == cap) throw new StackOverflowError();
        arr[++top] = n;
    }

    public int pop() {
        if (top == -1) throw new EmptyStackException();
        return arr[top--];
    }

    public int getDeepest(){
        return arr[0];
    }

    public int getTop(){
        return top;
    }
}