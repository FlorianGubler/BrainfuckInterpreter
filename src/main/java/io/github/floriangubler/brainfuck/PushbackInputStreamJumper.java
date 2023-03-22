package io.github.floriangubler.brainfuck;

import io.github.floriangubler.utils.ReducedIntStack;
import io.github.floriangubler.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
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

    private int curr;

    public PushbackInputStreamJumper(PushbackInputStream stream, int bufferSize, int markerLimit){
        this.stream = stream;
        this.buff = new byte[bufferSize];
        this.stack = new ReducedIntStack(markerLimit);
    }

    public int read() throws IOException {
        if(currRelPosition == currPosition) currPosition++;
        currRelPosition++;
        curr = stream.read();
        System.out.print((char) curr);
        if(stack.getTop() != -1 && currRelPosition == currPosition){
            Util.arrayAdd(buff, (byte) curr);
        }
        return curr;
    }

    public void mark(){
        if(stack.getTop() == -1) Util.arrayAdd(buff, (byte) curr);
        stack.push(currRelPosition);
    }

    public void back() throws IOException {
        int lastMarker = stack.pop();
        System.out.println();
        System.out.println(lastMarker);
        System.out.println(currRelPosition);
        for(byte b : buff){
            System.out.print((char) b);
        }
        System.out.println();
        stream.unread(buff, lastMarker - stack.getDeepest(), currRelPosition - lastMarker + 1);
        currRelPosition = lastMarker - 1;
        currPosition++;
        if(stack.getTop() == -1){
            Util.arrayClear(buff);
        }
    }

    public int getCurrRelPosition(){
        return this.currRelPosition;
    }
}
