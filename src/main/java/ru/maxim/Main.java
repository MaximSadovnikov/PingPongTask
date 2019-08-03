package ru.maxim;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception{
        Executor executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> ping = CompletableFuture.runAsync(() ->
                System.out.println("ping"), executor);
        CompletableFuture<Void> pong = CompletableFuture.runAsync(() ->
                System.out.println("pong"), executor);

    }

}
