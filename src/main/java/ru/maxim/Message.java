package ru.maxim;

/*
TODO:
    порядок не гарантирован, можен сначала напечатать pong а потом ping во время старта
    spurious wakeups приводит к двойному ping ping
    последний поток остается "навечно" висящим и ожидающим пока кто-то сделает notify
*/
public class Message implements Runnable {

    private static final int N = 10;
    private Thread thread;
    private int i;
    private static final Object object = new Object();

    private Message(String name){
        thread = new Thread(this, name);
        thread.start();
        i = 0;
    }

    public void run(){
        while (i < N){
            synchronized (object) {
                System.out.println(i + "\t" + thread.getName());
                i++;
                object.notifyAll();
                try {
                    object.wait(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread pingMessage = new Thread(new Message("Ping"));
        Thread pongMessage = new Thread(new Message("Pong"));
        pingMessage.join();
        pongMessage.join();

    }
}