package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        char[] process = new char[]{'-', '\\', '|', '/'};
        int index = 0;

        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\rLoading ... " + process[index]);
            index = (index + 1) % process.length;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.print("\rLoading ... Done!\n");
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress.interrupt();
    }
}
