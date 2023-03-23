package io.github.floriangubler.brainfuck;

import java.util.Arrays;

/*
 * @author Gubler Florian
 * A simple Exception class with storing the current state of the interpreter
 */
public class BrainFuckException extends Exception {
    /* Storing the current interpreter position */
    private final int interpretPos;

    /* Current ptr in memory */
    private final int ptr;

    /* Current memory */
    private final byte[] memory;

    /* Constructor with a exception message */
    public BrainFuckException(int interpretPos, int ptr, byte[] memory, String msg) {
        super(msg);
        this.interpretPos = interpretPos;
        this.ptr = ptr;
        this.memory = memory;
    }

    /* Constructor with a cause */
    public BrainFuckException(int interpretPos, int ptr, byte[] memory, Throwable e) {
        super(e);
        this.interpretPos = interpretPos;
        this.ptr = ptr;
        this.memory = memory;
    }

    /* Print out the Exception
    *  @param debug Enable flag to print out memory
    */
    public void printOut(boolean debug){
        if(this.getMessage() != null){
            System.err.println("Brainfuck Runtime Exception occured: " + this.getMessage());
        } else{
            System.err.println("Brainfuck Runtime Exception occured: " + this.getCause().getMessage());
        }
        System.err.println("At position: " + this.interpretPos);
        System.err.println("Pointer Position: " + this.ptr);
        System.err.print("Memory: ");
        if(debug){
            System.err.println(Arrays.toString(memory));
        } else{
            System.err.println("Enable debug to output full memory");
        }
    }
}
