package io.github.floriangubler.brainfuck;

import io.github.floriangubler.utils.Util;

import java.io.*;
import java.util.Arrays;

public class Interpreter {
    private int ptr = 0; // Data pointer

    // Max memory limit. It is the highest number which
    // can be represented by an unsigned 16-bit binary
    // number.
    public static final int MAX_MEM = 65535;

    // Array of byte type simulating memory of half max
    // other half is for buffering
    private final byte[] memory = new byte[MAX_MEM / 2];

    //Interpreter Position from InputStream
    private int counter = 0;

    //Loop buff 1/4 of memory
    private byte[] loopBuff = new byte[MAX_MEM / 4];

    public static void brainfuck(InputStream inputStream) throws IOException, BrainFuckException {
        System.out.println("");
        new Interpreter().interpret(inputStream);
    }

    private void interpret(InputStream inputStream) throws BrainFuckException {
        //Pusback Input Stream to buffer loops. Max Buffer is half memory minus Storage for jump tables
        PushbackInputStream pbInputStream = new PushbackInputStream(inputStream, (MAX_MEM / 4));
        LoopState loopState = LoopState.NO_LOOP;
        int r;
        try {
            while ((r = pbInputStream.read()) != -1) {
                char ch = (char) r;
                if(loopState == LoopState.LOOP){
                    Util.arrayAdd(loopBuff, (byte) r);
                }
                if(loopState != LoopState.SKIP || ch == ']') {
                    switch (ch) {
                        case '>':
                            ptr++;
                            break;
                        case '<':
                            ptr--;
                            break;
                        case '+':
                            memory[ptr]++;
                            break;
                        case '-':
                            memory[ptr]--;
                            break;
                        case '.':
                            System.out.print((char) memory[ptr]);
                            break;
                        case ',':
                            int inp = System.in.read();
                            if (inp == -1) System.err.println("No more input");
                            memory[ptr] = (byte) inp;
                            break;
                        case '[':
                            if (memory[ptr] == 0) {
                                loopState = LoopState.SKIP;
                            } else {
                                loopState = LoopState.LOOP;
                            }
                            break;
                        case ']':
                            if (loopState == LoopState.SKIP) {
                                loopState = LoopState.NO_LOOP;
                            } else if (loopState == LoopState.LOOP) {
                                if (memory[ptr] != 0) {
                                    pbInputStream.unread(loopBuff, 0, Util.arrayLength(loopBuff));
                                    //Clear value
                                    loopBuff = new byte[MAX_MEM / 4];
                                } else{
                                    loopState = LoopState.NO_LOOP;
                                    loopBuff = new byte[MAX_MEM / 4];
                                }
                            } else {
                                throw new BrainFuckException(counter, ptr, memory, "Invalid loop format");
                            }
                            break;
                        default:
                            counter--; //Because count only on real BF char
                    }
                }
                counter++;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            throw new BrainFuckException(counter, ptr, memory, new OutOfMemoryError("BF Out of Memory error"));
        } catch (Exception e1){
            throw new BrainFuckException(counter, ptr, memory, e1);
        }
    }

    private enum LoopState{
        SKIP,
        LOOP,
        NO_LOOP;
    }
}
