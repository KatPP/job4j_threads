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

        /**
         *  Здесь добавил вывод состояния, очень наглядно, и небольшая пауза чтобы не перегружать процессор.
         */

        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.println("Состояние первой нити: " + first.getState());
            System.out.println("Состояние второй нити: " + second.getState());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Работа завершена.");
    }
}