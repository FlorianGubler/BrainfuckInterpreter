package io.github.floriangubler.brainfuck;

import java.io.*;

public class Interpreter {
    private int ptr = 0; // Data pointer

    // Max memory limit. It is the highest number which
    // can be represented by an unsigned 16-bit binary
    // number.
    public static final int MAX_MEM = 65535;

    // Array of byte type simulating memory of half max
    // other half is for buffering
    private final byte[] memory = new byte[MAX_MEM / 2];

    public static void brainfuck(InputStream inputStream) throws IOException, BrainFuckException {
        System.out.println("");
        new Interpreter().interpret(inputStream);
    }

    private void interpret(InputStream inputStream) throws BrainFuckException {
        //Pusback Input Stream to buffer loops. Max Buffer is half memory minus Storage for jump tables
        PushbackInputStreamJumper jumper = new PushbackInputStreamJumper(new PushbackInputStream(inputStream, MAX_MEM / 4), ((MAX_MEM / 4) - (100 / 4)), 100);
        LoopState loopState = LoopState.NO_LOOP;
        int r;
        try {
            while ((r = jumper.read()) != -1) {
                char ch = (char) r;
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
                            } else{
                                jumper.mark();
                                loopState = LoopState.LOOP;
                            }
                            break;
                        case ']':
                            if (loopState == LoopState.SKIP) {
                                loopState = LoopState.NO_LOOP;
                            } else if (loopState == LoopState.LOOP) {
                                if (memory[ptr] != 0) {
                                    jumper.back();
                                } else{
                                    loopState = LoopState.NO_LOOP;
                                }
                            } else {
                                throw new BrainFuckException(jumper.getCurrRelPosition(), ptr, memory, "Invalid loop format");
                            }
                            break;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            throw new BrainFuckException(jumper.getCurrRelPosition(), ptr, memory, new OutOfMemoryError("BF Out of Memory error"));
        } catch (Exception e1){
            e1.printStackTrace();
            throw new BrainFuckException(jumper.getCurrRelPosition(), ptr, memory, e1);
        }
    }

    private enum LoopState{
        SKIP,
        LOOP,
        NO_LOOP;
    }
}
