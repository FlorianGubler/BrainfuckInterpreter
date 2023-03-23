package io.github.floriangubler.brainfuck;

import java.io.*;

/*
 * @author Gubler Florian
 * Class for Brainfuck Interpretation
 * Implements all actions in BrainFuck for each character
 * Works with an inputStream manged by a PushbackInputStreamJumper
 */
public class Interpreter {
    /* Memory pointer */
    private int ptr = 0;

    /* Max memory limit. It is the highest number which
     * can be represented by an unsigned 16-bit binary
     * number.
     */
    public static final int MAX_MEM = 65535;

    /*
     * Array of byte type simulating memory of half max
     * other half is for buffering
     */
    private final byte[] memory = new byte[MAX_MEM / 2];

    /* Start brainfuck interpretation with an inputStream */
    public static void brainfuck(InputStream inputStream) throws IOException, BrainFuckException {
        new Interpreter().interpret(inputStream);
    }

    /* Start real brainfuck interpretation */
    private void interpret(InputStream inputStream) throws BrainFuckException {
        //Pusback Input Stream to buffer loops. Max Buffer is half memory minus Storage for jump tables
        PushbackInputStreamJumper jumper = new PushbackInputStreamJumper(new PushbackInputStream(inputStream, MAX_MEM / 4), ((MAX_MEM / 4) - (100 / 4)), 100);
        //Set loopState to NO_LOOP
        LoopState loopState = LoopState.NO_LOOP;
        int r;
        try {
            //Read inputStream
            while ((r = jumper.read()) != -1) {
                //Get char from byte
                char ch = (char) r;
                //Only interpret current char if not in LOOP SKIP and not a loop closing char
                if(loopState != LoopState.SKIP || ch == ']') {
                    switch (ch) {
                        //Increases pointer by 1
                        case '>':
                            ptr++;
                            break;
                        //Decreases pointer by 1
                        case '<':
                            ptr--;
                            break;
                        //Increases current pointed memory by 1
                        case '+':
                            memory[ptr]++;
                            break;
                        //Decreases current pointed memory by 1
                        case '-':
                            memory[ptr]--;
                            break;
                        //Outputs current pointer memory as character
                        case '.':
                            System.out.print((char) memory[ptr]);
                            break;
                        //Reads a new byte from input
                        case ',':
                            int inp = System.in.read();
                            if (inp == -1) System.err.println("No more input");
                            memory[ptr] = (byte) inp;
                            break;
                        //Opens loop
                        case '[':
                            if (memory[ptr] == 0) {
                                loopState = LoopState.SKIP;
                            } else{
                                jumper.mark();
                                loopState = LoopState.LOOP;
                            }
                            break;
                        //Closes loop
                        case ']':
                            if (loopState == LoopState.SKIP) {
                                loopState = LoopState.NO_LOOP;
                            } else if (loopState == LoopState.LOOP) {
                                //If not loop ended, jump back
                                if (memory[ptr] != 0) {
                                    jumper.back();
                                } else{
                                    //Remove last marker, because duplicate marked
                                    jumper.popMarker();
                                    //Only set to NO_LOOP if not in nested loop
                                    if(!jumper.isMarked()){
                                        loopState = LoopState.NO_LOOP;
                                    }
                                }
                            } else {
                                throw new BrainFuckException(jumper.getPos(), ptr, memory, "Invalid loop format");
                            }
                            break;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            throw new BrainFuckException(jumper.getPos(), ptr, memory, new OutOfMemoryError("BF Out of Memory error"));
        } catch (Exception e1){
            throw new BrainFuckException(jumper.getPos(), ptr, memory, e1);
        }
    }

    /* Enum for current Loop State */
    private enum LoopState{
        /* Loop is skipped - bytes not interpreted until loop closed */
        SKIP,
        /* Currently in loop */
        LOOP,
        /* Currently not in a loop */
        NO_LOOP;
    }
}
