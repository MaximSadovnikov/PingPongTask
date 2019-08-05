package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
TODO:
    spurious wakeups
    порядок не гарантирован, может сначала напечатать pong
    последний поток остается "навечно" висящим и ожидающим пока кто-то сделает notify
 */
class PingPong implements Runnable {
    private static final Object object = new Object();
    private int i;
    private String word;

    private PingPong(String s) {
        word = s;
        i = 0;
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new PingPong("| ping\t\t |"));
        service.execute(new PingPong("| \t\tpong |"));
        service.shutdown();
    }

    public void run() {
        int n = 30;
        do {
            synchronized (object) {
                System.out.println(word + i);
                object.notifyAll();
                try {
                    object.wait(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } while (i++ < n);
    }
}
