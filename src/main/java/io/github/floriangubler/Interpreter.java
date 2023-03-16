package io.github.floriangubler;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Interpreter {
    private int ptr; // Data pointer

    // Max memory limit. It is the highest number which
    // can be represented by an unsigned 16-bit binary
    // number.
    public static final int MAX_MEM = 65535;

    // Array of byte type simulating memory of max
    // 65535 bits from 0 to 65534.
    private final byte[] memory = new byte[MAX_MEM / 2];

    //Buffer
    private final char[][][] loopBuffer = new char[500][500][((MAX_MEM / 2) / 16) - 1000];

    public static void brainfuck(InputStream inputStream) throws IOException, BrainFuckException {
        System.out.println("");
        new Interpreter().interpret(inputStream, Charset.defaultCharset());
    }

    public static void brainfuck(InputStream inputStream, Charset charset) throws IOException, BrainFuckException {
        System.out.println("");
        new Interpreter().interpret(inputStream, charset);
    }

    public void interpret(InputStream intputStream, Charset charset) throws BrainFuckException, IOException {
        interpret(intputStream, charset, 0);
    }

    private void interpret(InputStream inputStream, Charset charset, int loopDepth) throws IOException, BrainFuckException {
        //Buffered reader for efficiency
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        LoopState loopState = LoopState.NO_LOOP;
        int counter = 0;
        int loopCounter = 0;
        int r;
        while ((r = reader.read()) != -1) {
            counter++;
            char ch = (char) r;
            if(loopState == LoopState.BUFFER && ch != ']' && ch != '['){
                array_add(loopBuffer[loopDepth][loopCounter], ch);
            }
            if(((loopState != LoopState.SKIP || ch == ']') && (loopState != LoopState.BUFFER || ch == ']'))){
                switch(ch){
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
                        if(inp == -1) System.err.println("No more input");
                        memory[ptr] = (byte) inp;
                        break;
                    case '[':
                        if(memory[ptr] == 0){
                            loopState = LoopState.SKIP;
                        } else{
                            loopState = LoopState.BUFFER;
                        }
                        break;
                    case ']':
                        if(loopState == LoopState.SKIP){
                            loopState = LoopState.NO_LOOP;
                        } else if (loopState == LoopState.BUFFER){
                            while(memory[ptr] != 0){
                                interpret(new ByteArrayInputStream(charsToBytes(loopBuffer[loopDepth][loopCounter], charset)), charset, loopDepth + 1);
                            }
                            loopState = LoopState.NO_LOOP;
                            loopCounter++;
                        } else{
                            throw new BrainFuckException(counter, ptr, memory, "Invalid loop format");
                        }
                        break;
                }
            }
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
