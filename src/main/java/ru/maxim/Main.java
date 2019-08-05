package ru.maxim;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
TODO:
    решение может сначала напечатать pong а потом ping
    решение не ждет заверешения потоков и может вообще ничего не напечатать
*/

public class Main {
    public static void main(String[] args) throws Exception{
        ExecutorService service = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> ping = CompletableFuture.runAsync(() ->
                System.out.println("ping"), service);
        CompletableFuture<Void> pong = CompletableFuture.runAsync(() ->
                System.out.println("pong"), service);
        ping.get();
        pong.get();
        service.shutdown();
    }
}
