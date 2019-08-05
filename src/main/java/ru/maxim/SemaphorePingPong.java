package ru.maxim;

import java.util.concurrent.Semaphore;


/*
TODO: сделать ping pong с помощью синхронизации за счет semaphore api было бы хорошо
  начало с того что нам нужно два pingSem и pongSem норм
  а вот как и кто будет делать release / acquire подумай
*/

public class SemaphorePingPong {
    private Semaphore semPing = new Semaphore(1);
    private Semaphore semPong = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        SemaphorePingPong pingPong = new SemaphorePingPong();
        int N = 10;
        Thread ping = new Thread(new Ping(pingPong, N));
        Thread pong = new Thread(new Pong(pingPong, N));
        ping.start();
        pong.start();
        ping.join();
        pong.join();
    }

    void printPing(int i) {
        try {
            semPing.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(i + "\tPing");
        semPong.release();
    }

    void printPong(int i) {
        try {
            semPong.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(i + "\tPong");
        semPing.release();
    }
}

class Ping implements Runnable {
    private SemaphorePingPong pingPong;
    private int N;

    Ping(SemaphorePingPong pingPong, int N) {
        this.pingPong = pingPong;
        this.N = N;
    }

    @Override
    public void run() {
        for (int i = 0; i <= N; i++) {
            pingPong.printPing(i);
        }
    }
}

class Pong implements Runnable {
    private SemaphorePingPong pingPong;
    private int N;

    Pong(SemaphorePingPong pingPong, int N) {
        this.pingPong = pingPong;
        this.N = N;
    }

    @Override
    public void run() {
        for (int i = 0; i <= N; i++) {
            pingPong.printPong(i);
        }
    }
}