package ru.maxim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TextInThreads {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        final ExecutorService readService = Executors.newFixedThreadPool(1);
        final ExecutorService writeService = Executors.newFixedThreadPool(5);
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        final String path = "/Users/maksimsadovnikov/IdeaProjects/PingPongTask" +
                "/src/main/java/ru/maxim/Latin-Lipsum.txt";
//        Thread readThread = new Thread(new ReadFromThread(queue, path));
//        readThread.start();
        readService.submit(new ReadFromThread(queue, path));
        Thread.sleep(1000);
        while (!queue.isEmpty()) {
            writeService.submit(new WriteFromThread(queue));
        }
        readService.shutdown();
        writeService.shutdown();
    }
}

class ReadFromThread implements Runnable {

    private final Scanner scanner;
    private SynchronousQueue<String> queue;

    public ReadFromThread(SynchronousQueue<String> queue, String path) throws FileNotFoundException {
        this.queue = queue;
        this.scanner = new Scanner(new File(path));
    }

    @Override
    public void run() {
        while (scanner.hasNext()) {
            try {
                queue.put(scanner.next());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class WriteFromThread implements Runnable {

    private SynchronousQueue<String> queue;
    private final AtomicInteger index = new AtomicInteger(0);

    public WriteFromThread(SynchronousQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        index.incrementAndGet();
        try {
            System.out.println(queue.take() + "\t\t" + index);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}