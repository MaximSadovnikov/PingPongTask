package ru.maxim;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
TODO:
    порядок не гарантирован, можен сначала напечатать pong а потом ping во время старта
    spurious wakeups приводит к двойному ping ping
    последний поток остается "навечно" висящим и ожидающим пока кто-то сделает notify
*/
public class Message implements Runnable {


    private BlockingQueue<String> queue;
    private static final int N = 10;
    private Thread thread;
    private int i;
    private static final Object object = new Object();


    private Message(String name, BlockingQueue<String> queue) {
        thread = new Thread(this, name);
        thread.start();
        this.queue = queue;
        i = 0;
    }

    public void run() {
        while (i < N) {
            synchronized (object) {
                System.out.println(i + "\t" + thread.getName());
                i++;
                object.notifyAll();
                queue.poll();
                queue.add(thread.getName());
                while (queue.peek().equals(thread.getName())) {
                    try {
                        object.wait(10);
                        if (i == N) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }


            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(1);
        Thread pingMessage = new Thread(new Message("Ping", queue));
        Thread pongMessage = new Thread(new Message("Pong", queue));
        pingMessage.join();
        pongMessage.join();

    }
}