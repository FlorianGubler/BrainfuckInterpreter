package io.github.floriangubler.brainfuck;

import io.github.floriangubler.utils.ReducedIntStack;
import io.github.floriangubler.utils.Util;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

/*
 * @author Florian Gubler
 * A Jumper with a Stack of Jump Points for BF Nested Loop Implementation.
 * After marking the class buffers the input stream, so you can jump back to the last marking point.
 */
public class PushbackInputStreamJumper {

    /* The PusbackInputStream to work with */
    private final PushbackInputStream stream;

    /* The marker stack */
    private final ReducedIntStack stack;

    /* The marker buffer */
    private final byte[] buff;

    /* Current relative position to pushbacks in stream */
    private int currRelPosition = 0;

    /* Current relative position in stream */
    private int currPosition = 0;
    public PushbackInputStreamJumper(PushbackInputStream stream, int bufferSize, int markerLimit){
        this.stream = stream;
        this.buff = new byte[bufferSize];
        this.stack = new ReducedIntStack(markerLimit);
    }

    public int read() throws IOException {
        currRelPosition++;
        currPosition++;
        int curr = stream.read();
        System.out.print((char) curr);
        if(stack.getTop() != -1 && currRelPosition == currPosition){
            Util.arrayAdd(buff, (byte) curr);
        }
        return curr;
    }

    public void pushMarker(){
        stack.push(currRelPosition);
    }

    public void goToLastMarker() throws IOException {
        int lastMarker = stack.pop();
        System.out.println(Arrays.toString(buff));
        stream.unread(buff, lastMarker - stack.getDeepest(), currRelPosition - lastMarker + 1);
        if(stack.getTop() == -1){
            Util.arrayClear(buff);
        }
        currRelPosition = lastMarker;
    }
}
