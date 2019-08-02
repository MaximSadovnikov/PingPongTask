package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphorePingPong {

    Semaphore pingSem;
    Semaphore pongSem;

    public SemaphorePingPong() {
        pingSem = new Semaphore(1);
        pongSem = new Semaphore(0);
    }

    public void pingPrint (Runnable pingRun) throws InterruptedException {
        pingSem.acquire();
        pingRun.run();
        pingSem.release();
    }

    public void pongPrint (Runnable pongRun) throws InterruptedException {
        pongSem.acquire();
        pongRun.run();
        pongSem.release();
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        SemaphorePingPong semaphorePingPong = new SemaphorePingPong();
//        for (int i = 0; i < 10; i++) {
//            semaphorePingPong.pingPrint(() -> );
//        }
    }
}