package ru.maxim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
TODO:
    не понял почему статическую not thread safe переменную инкрементим
    искользовать executorService в качестве очереди задач ок
*/


class Foo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(5);
        AtomicInteger index = new AtomicInteger(0);
        while (index.get() < 1000) {
            if (index.get() < 444) {
                service.submit(new TaskForThread(String.valueOf(index.get()), true));
            } else {
                service.submit(new TaskForThread(String.valueOf(index.get()), false));
            }
            index.incrementAndGet();
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

