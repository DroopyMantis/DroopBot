package com.droopymantis.droopbot;

import com.droopymantis.droopbot.commands.CommandManager;

public class Queue {

    private String[] arr;
    private  String[] atArr;

    public Queue() {
        arr = new String[0];
        atArr = new String[0];
    }

    public Queue(int players) {
        arr = new String[players];
        atArr = new String[players];
    }

    public void joinQueue(String name, String atName){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null){
                arr[i] = name;
                break;
            }
        }
        for(int i = 0; i < atArr.length; i++){
            if(atArr[i] == null){
                atArr[i] = atName;
                break;
            }
        }
    }

    public String getStatus(){
        String str = "";
        for(int i = 0; i < arr.length; i++){
            if(arr[i] != null){
                str = str + "\n" + arr[i];
            }
        }
        return str;
    }

    public void leaveQueue(String name, String atName){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(name)){
                arr[i] = null;
                break;
            }
        }
        for(int i = 0; i < atArr.length; i++){
            if(atArr[i].equals(atName)){
                atArr[i] = null;
                break;
            }
        }
    }

    public String getAts(){
        String str = "";
        for(String i : atArr){
            str = str + i;
        }
        return str;
    }

    public boolean checkUser(String name){
        for(String x : arr){
            if(x != null){
                if(x.equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkFull(){
        for(String x : arr){
            if(x == null){
                return false;
            }
        }
        return true;
    }

    public void print(){
        for(String x : arr){
            System.out.println(x);
        }
    }
}