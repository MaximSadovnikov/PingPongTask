package ru.maxim;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;
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
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        final URL path = TextInThreads.class.getResource("/Latin-Lipsum.txt");

        ReadFromThread reader = new ReadFromThread(queue, path, countDownLatch);
        WriteFromThread writer = new WriteFromThread(queue);
        readService.submit(reader);
        countDownLatch.await();
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
    private CountDownLatch countDownLatch;

    ReadFromThread(Queue<String> queue, URL path, CountDownLatch countDownLatch) throws IOException {
        this.queue = queue;
        this.scanner = new Scanner(path.openStream());
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        while (scanner.hasNext()) {
            queue.add(scanner.next());
            countDownLatch.countDown();
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
            System.out.format("%4d %20s %20s\n",
                    (index.get()),
                    Thread.currentThread().getName(),
                    queue.poll());
        }
    }
}