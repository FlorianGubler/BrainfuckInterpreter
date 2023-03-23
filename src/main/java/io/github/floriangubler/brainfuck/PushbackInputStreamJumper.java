package io.github.floriangubler.brainfuck;

import io.github.floriangubler.utils.ReducedIntStack;

import java.io.IOException;
import java.io.PushbackInputStream;

/*
 * @author Florian Gubler
 * A Jumper with a Stack of Jump Points for BrainFuck Nested Loop Implementation with a stream
 * After marking, the class buffers the input stream automatically, so you can jump back to the last marking point without worrying about data
 */
public class PushbackInputStreamJumper {

    /* The PushbackInputStream to work with */
    private final PushbackInputStream stream;

    /* The marker stack */
    private final ReducedIntStack stack;

    /* The marker buffer */
    private final byte[] buff;

    /* Current relative position to pushbacks in stream */
    private int pos = 0;

    /* The current byte from stream */
    private int curr;

    /* Constructor for the PushbackInputStreamJumper */
    public PushbackInputStreamJumper(PushbackInputStream stream, int bufferSize, int markerLimit){
        this.stream = stream;
        this.buff = new byte[bufferSize];
        this.stack = new ReducedIntStack(markerLimit);
    }

    /* Reads next byte from stream */
    public int read() throws IOException {
        //Increase position in stream
        pos++;
        //Read new byte from stream
        curr = stream.read();
        //If marked and current byte not already buffered, buffer current byte
        if(isMarked() && buff[pos] == 0){
            buff[pos] = (byte) curr;
        }
        //Return current byte
        return curr;
    }

    /* Marks the current position in stream */
    public void mark(){
        //Because stack is empty when check to buff in read()
        if(stack.getTop() == -1) buff[pos] = (byte) curr;
        //Push pos to marker stack
        stack.push(pos);
    }

    /* Jumps back to last marked position and removes it */
    public void back() throws IOException {
        // Pop last marker from marker stack
        int lastMarker = stack.pop();
        // Unread to last marker from stream
        stream.unread(buff, lastMarker, pos - lastMarker + 1);
        //Reset position to lastMarker - 1 because read() increases it
        pos = lastMarker - 1;
    }

    /* Get current relative position in stream */
    public int getPos(){
        return this.pos;
    }

    /* Check if stream is marked */
    public boolean isMarked(){
        return this.stack.getTop() != -1;
    }

    /* Delete the last marker */
    public void popMarker(){
        this.stack.pop();
    }
}
