package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Message implements Runnable {

    private static final int N = 10;
    private Thread thread;
    private static Object object = new Object();

    public Message(String name){
        thread = new Thread(this, name);
        thread.start();
    }

    public void run(){
        for(int i=0; i<N; i++){
            synchronized (object) {
                System.out.println(i + "\t" + thread.getName());
                object.notify();
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Thread message1 = new Thread(new Message("Ping"));
        Thread message2 = new Thread(new Message("Pong"));
        service.shutdown();
    }
}