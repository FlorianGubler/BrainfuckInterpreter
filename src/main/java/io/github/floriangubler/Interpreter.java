package io.github.floriangubler;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class Interpreter {
    private static int ptr; // Data pointer

    // Max memory limit. It is the highest number which
    // can be represented by an unsigned 16-bit binary
    // number. Many computer programming environments
    // beside brainfuck may have predefined
    // constant values representing 65535.
    private static final int length = 65535;

    // Array of byte type simulating memory of max
    // 65535 bits from 0 to 65534.
    private static byte memory[] = new byte[length];

    public static void interpret(File inputFile) throws IOException {
        interpret(new FileInputStream(inputFile), Charset.defaultCharset());
    }

    public static void interpret(InputStream inputStream, Charset charset) throws IOException {
        //Buffered reader for efficiency
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
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
                case '.':
                    System.out.println((char) memory[ptr]);
                case ',':
                    int inp = System.in.read();
                    if(inp == -1) System.err.println("No more input");
                    memory[ptr] = (byte) inp;
            }
        }
    }
}
