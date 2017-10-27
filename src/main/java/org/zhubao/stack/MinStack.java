package org.zhubao.stack;

import java.util.Stack;

public class MinStack {

    private Stack<Integer> stack = new Stack<Integer>();
    
    private Stack<Integer> minStack = new Stack<Integer>();
    
    public void push(int num) {
        stack.push(num);
        
        if(minStack.isEmpty() || minStack.peek() > num) {
            minStack.push(num);
        }
    }
    
    public void pop() {
        
        if(stack.isEmpty()) {
            return;
        }
        
        int num = stack.pop();
        
        if(num == minStack.peek()) {
            minStack.pop();
        }
    }
    
    public int min() {
        return minStack.pop();
    }
    
    public static void main(String[] args) {
        
        MinStack minStack = new MinStack();
        
        minStack.push(5);
        
        minStack.push(7);
        
        minStack.push(3);
        
        minStack.push(2);
        
        minStack.push(4);
        
        minStack.push(9);
        
        minStack.push(1);
        
        minStack.pop();
        
        System.out.println(minStack.min());
    }
}
