package io.github.floriangubler.utils;

import java.util.EmptyStackException;

/*
 * @author Gubler Florian
 * Simple int stack becuase java.util.Stack is overloaded
 */
public class ReducedIntStack {
    /* The stack array */
    private final int[] arr;

    /* The stack capacity (size) */
    private final int capacity;

    /* The current top of the stack */
    private int top = -1;

    /* Constructor for the int stack with a size */
    public ReducedIntStack(int size) {
        capacity = size;
        arr = new int[size];
    }

    /* Method for pushing a new int to the stack */
    public void push(int n) {
        if (top == capacity) throw new StackOverflowError();
        arr[++top] = n;
    }

    /* Method for pop the top element of the stack */
    public int pop() {
        if (top == -1) throw new EmptyStackException();
        return arr[top--];
    }

    /* Get the current top of the stack */
    public int getTop(){
        return top;
    }
}