package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PingPong implements Runnable {
    private int i;
    private String word;
    private static final Object object = new Object();

    private PingPong(String s) {
        word = s;
        i = 0;
    }

    public void run() {
        int n = 30;
        do {
            synchronized (object) {
                System.out.println(word + i);
                object.notifyAll();
                try {
                    object.wait();
                } catch (InterruptedException e) { break; }
            }
        } while (i++ < n);
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new PingPong("| ping\t\t |"));
        service.submit(new PingPong("| \t\tpong |"));
        service.shutdown();
    }
}
