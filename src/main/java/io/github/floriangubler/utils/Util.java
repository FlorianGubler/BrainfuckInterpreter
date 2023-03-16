package io.github.floriangubler.utils;

public class Util {
    public static void arrayAdd(int[] arr, int o){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                arr[i] = o;
                break;
            }
        }
    }

    public static void arrayAdd(byte[] arr, byte o){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                arr[i] = o;
                break;
            }
        }
    }

    public static void arrayClear(byte[] arr){
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i] == 0){
                break;
            } else{
                arr[i] = 0;
            }
        }
    }
}
