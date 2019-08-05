package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PingPong implements Runnable {
    int N = 30, i;
    String word;
    private static Object object = new Object();

    PingPong(String s) {
        word = s;
        i = 0;
    }

    public void run() {
         do {
            synchronized (object) {
                System.out.println(word + i);
                object.notifyAll();
                try {
                    object.wait();
                } catch (InterruptedException e) { break; }
            }
        } while (i++ < N);
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new PingPong("| ping\t\t |"));
        service.submit(new PingPong("| \t\tpong |"));
        service.shutdown();
    }
}
