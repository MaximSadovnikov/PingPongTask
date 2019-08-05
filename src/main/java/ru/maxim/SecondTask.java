package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


class Foo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1000; i++) {
            if (i < 444) {
                service.submit(new TaskForThread(String.valueOf(i), true));
            } else {
                service.submit(new TaskForThread(String.valueOf(i), false));
            }
        }
        service.shutdown();
    }
}

class TaskForThread implements Runnable {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private static int value = 0;
    private String string;
    private boolean signPlus;

    TaskForThread(String s, boolean sign) {
        this.string = s;
        this.signPlus = sign;
    }

    @Override
    public void run() {
        if (signPlus) {
            atomicInteger.incrementAndGet();
            value++;
            System.out.println(Thread.currentThread().getName() + " \ti = " + string +
                    ", atomic = " + atomicInteger.get() +
                    ", value = " + value);
        } else {
            atomicInteger.decrementAndGet();
            value--;
            System.out.println(Thread.currentThread().getName() + " \ti = " + string +
                    ", atomic = " + atomicInteger.get() +
                    ", value = " + value);
        }
    }
}

