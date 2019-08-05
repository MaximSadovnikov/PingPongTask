package ru.maxim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TextInThreads {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        final ExecutorService readService = Executors.newFixedThreadPool(1);
        final ExecutorService writeService = Executors.newFixedThreadPool(5);
        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        // write your path
        final String path = "/Users/maksimsadovnikov/IdeaProjects/PingPongTask" +
                "/src/main/java/ru/maxim/Latin-Lipsum.txt";
        Scanner scanner = new Scanner(new File(path));
        ReadFromThread reader = new ReadFromThread(queue, path);
        WriteFromThread writer = new WriteFromThread(queue);
        AtomicInteger i = new AtomicInteger(0);
        readService.submit(reader);
        while (queue.isEmpty()) {
            Thread.sleep(1);
        }
        while (!queue.isEmpty()) {
            writeService.submit(writer);
        }
        readService.shutdown();
        writeService.shutdown();
    }
}

class ReadFromThread implements Runnable {

    private final Scanner scanner;
    private Queue<String> queue;

    ReadFromThread(Queue<String> queue, String path) throws FileNotFoundException {
        this.queue = queue;
        this.scanner = new Scanner(new File(path));
    }

    @Override
    public void run() {
        while (scanner.hasNext()) {
            queue.add(scanner.next());
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            return;
        }
        Thread.currentThread().interrupt();
    }
}

class WriteFromThread implements Runnable {

    private Queue<String> queue;
    private final AtomicInteger index = new AtomicInteger(0);

    WriteFromThread(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        if (queue.peek() != null) {
            index.incrementAndGet();
            System.out.format("%4d\t\t\t%s\t\t\t%s\n",
                    (index.get()),
                    Thread.currentThread().getName(),
                    queue.poll());
        }
    }
}