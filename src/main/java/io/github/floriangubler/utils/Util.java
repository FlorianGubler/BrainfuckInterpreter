package io.github.floriangubler.utils;

public class Util {
    public static void arrayAdd(byte[] arr, byte o){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                arr[i] = o;
                break;
            }
        }
    }

    public static int arrayLength(byte[] arr){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                return i;
            }
        }
        return arr.length;
    }
}
