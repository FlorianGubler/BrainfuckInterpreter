package io.github.floriangubler;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Interpreter {
    private int ptr; // Data pointer

    // Max memory limit. It is the highest number which
    // can be represented by an unsigned 16-bit binary
    // number.
    public static final int MAX_MEM = 65535;

    // Array of byte type simulating memory of half max
    // other half is for buffering
    private final byte[] memory = new byte[MAX_MEM / 2];

    //Interpreter Position from InputStream
    private int counter = 0;

    public static void brainfuck(InputStream inputStream) throws IOException, BrainFuckException {
        new Interpreter().interpret(inputStream, Charset.defaultCharset());
    }

    public static void brainfuck(InputStream inputStream, Charset charset) throws IOException, BrainFuckException {
        new Interpreter().interpret(inputStream, charset);
    }

    private void interpret(InputStream intputStream, Charset charset) throws BrainFuckException, IOException {
        interpret(intputStream, charset, 0);
    }

    private void interpret(InputStream inputStream, Charset charset, int loopDepth) throws BrainFuckException {
        //Buffered reader for efficiency
        PushbackInputStream pbInputStream = new PushbackInputStream(inputStream, MAX_MEM / 2);
        LoopState loopState = LoopState.NO_LOOP;
        int loopCounter = 0;
        int r;
        try {
            while ((r = pbInputStream.read()) != -1) {
                counter++;
                char ch = (char) r;
                if (((loopState != LoopState.SKIP || ch == ']') && (loopState != LoopState.BUFFER || ch == ']'))) {
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
                                loopState = LoopState.BUFFER;
                            }
                            break;
                        case ']':
                            if (loopState == LoopState.SKIP) {
                                loopState = LoopState.NO_LOOP;
                            } else if (loopState == LoopState.BUFFER) {
                                while (memory[ptr] != 0) {

                                }
                                loopState = LoopState.NO_LOOP;
                                loopCounter++;
                            } else {
                                throw new BrainFuckException(counter, ptr, memory, "Invalid loop format");
                            }
                            break;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            throw new BrainFuckException(counter, ptr, memory, new OutOfMemoryError());
        } catch (Exception e1){
            throw new BrainFuckException(counter, ptr, memory, e1);
        }
    }

    private byte[] charsToBytes(char[] chars, Charset charset) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = charset.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    private void array_add(char[] arr, char o){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                arr[i] = o;
                break;
            }
        }
    }

    private enum LoopState{
        SKIP,
        BUFFER,
        NO_LOOP;
    }
}
