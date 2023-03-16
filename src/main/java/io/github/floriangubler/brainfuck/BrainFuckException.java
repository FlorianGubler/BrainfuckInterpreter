package io.github.floriangubler.brainfuck;

import java.lang.reflect.Array;
import java.util.Arrays;

public class BrainFuckException extends Exception {
    private final int interpretPos;
    private final int ptr;
    private final byte[] memory;

    public BrainFuckException(int interpretPos, int ptr, byte[] memory, String msg) {
        super(msg);
        this.interpretPos = interpretPos;
        this.ptr = ptr;
        this.memory = memory;
    }

    public BrainFuckException(int interpretPos, int ptr, byte[] memory, Throwable e) {
        super(e);
        this.interpretPos = interpretPos;
        this.ptr = ptr;
        this.memory = memory;
    }

    public void printOut(){
        if(this.getMessage() != null){
            System.err.println("Brainfuck Runtime Exception occured: " + this.getMessage());
        } else{
            System.err.println("Brainfuck Runtime Exception occured: " + this.getCause().getMessage());
        }
        System.err.println("At position: " + this.interpretPos);
        System.err.println("Program data: ");
        System.err.println("Pointer Position: " + this.ptr);
        System.err.println("Memory: ");
        System.err.println(Arrays.toString(memory));
    }
}
