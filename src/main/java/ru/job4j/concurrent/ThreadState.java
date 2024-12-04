package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(() -> {
            System.out.println("Нить 1: " + Thread.currentThread().getName());
        }, "Первая нить");

        Thread second = new Thread(() -> {
            System.out.println("Нить 2: " + Thread.currentThread().getName());
        }, "Вторая нить");

        first.start();
        second.start();

        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Работа завершена.");
    }
}