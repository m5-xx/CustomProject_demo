package com.example;

public class MyClass {
    public static void main(String [] args){
//        System.out.print("---------------->");
//        System.out.print(leijia(1));
        int [] array={23,14,45,654,2,5,64};
        int[] ints = intarray(array);
        for(int i=0;i<ints.length;i++){
            System.out.print(ints[i]+" ");
        }
    }

    public static int [] intarray(int [] arr){
        //排好序的数组
        for(int i=0;i<arr.length-1;i++){
            for(int j=0;j<arr.length-i-1;j++){
                if(arr[j]>arr[j+1]){
                    int index;
                    index=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=index;
                }
            }
        }
        return arr;
    }




    private static int leijia(int i) {
        if(i==9){
            return i;
        }
        return i+leijia(i+2);
    }
}
