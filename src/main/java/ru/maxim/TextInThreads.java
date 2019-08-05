package ru.maxim;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
TODO:
    Делать ожидание за счет проверки раз в секунду очень плохая практика,
        стоит воспользоваться примитивом синхронизации
    который не будет будить поток лишний раз и сработает сразу по наступлению
        события а не через секунду
    это я про всякие Thread.sleep(1);
    Latin-Lipsum.txt следует положить в ресурсы и читать через Class::getResource +
*/

public class TextInThreads {
    public static void main(String[] args) throws IOException, InterruptedException {
        final ExecutorService readService = Executors.newFixedThreadPool(1);
        final ExecutorService writeService = Executors.newFixedThreadPool(5);
        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        // write your path
        final URL path = TextInThreads.class.getResource("/Latin-Lipsum.txt");
//        final String path = "/Users/maksimsadovnikov/IdeaProjects/PingPongTask" +
//                "/src/main/java/ru/maxim/Latin-Lipsum.txt";
        ReadFromThread reader = new ReadFromThread(queue, path);
        WriteFromThread writer = new WriteFromThread(queue);
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

    ReadFromThread(Queue<String> queue, URL path) throws IOException {
        this.queue = queue;
        this.scanner = new Scanner(path.openStream());
    }

    @Override
    public void run() {
        while (scanner.hasNext()) {
            queue.add(scanner.next());
        }
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