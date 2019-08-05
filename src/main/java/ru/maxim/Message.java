package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Message implements Runnable {

    private static final int N = 10;
    private Thread thread;
    private int i;
    private static final Object object = new Object();

    private Message(String name){
        thread = new Thread(this, name);
        thread.start();
        i = 0;
    }

    public void run(){
        while (i < N){
            synchronized (object) {
                System.out.println(i + "\t" + thread.getName());
                i++;
                object.notifyAll();
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread message1 = new Thread(new Message("Ping"));
        Thread message2 = new Thread(new Message("Pong"));
    }
}