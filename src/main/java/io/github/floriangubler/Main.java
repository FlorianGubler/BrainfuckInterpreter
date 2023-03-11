package io.github.floriangubler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try{
            Interpreter.brainfuck(new ByteArrayInputStream(new Scanner(System.in).nextLine().getBytes()));
        } catch (IOException e){
            e.printStackTrace();
        } catch (BrainFuckException e1){
            e1.printOut();
        }
    }
}