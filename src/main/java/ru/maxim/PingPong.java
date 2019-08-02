package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PingPong implements Runnable {
    String word;
    private static Object object = new Object();

    PingPong(String s) {
        word = s;
    }

    public void run() {
        for (int i = 0; i < 30; i++) {
            synchronized (object) {
                System.out.println(word + i);
                object.notifyAll();
                try {
                    object.wait();
                } catch (InterruptedException e) { break; }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new PingPong("| ping\t\t |"));
        service.submit(new PingPong("| \t\tpong |"));
        service.shutdown();
    }
}
