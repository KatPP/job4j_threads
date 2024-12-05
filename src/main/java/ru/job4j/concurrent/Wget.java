package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = createFileFromUrl(url);
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long startTime = System.currentTimeMillis();

            while ((bytesRead = input.read(dataBuffer)) != -1) {
                output.write(dataBuffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                if (totalBytesRead >= speed) {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    if (timeTaken < 1000) {
                        long pause = 1000 - timeTaken;
                        Thread.sleep(pause);
                    }

                    totalBytesRead = 0;
                    startTime = System.currentTimeMillis();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes downloaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFileFromUrl(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return new File(fileName.isEmpty() ? "downloaded_file" : fileName);
    }

    private static void validateArguments(String[] args) throws IllegalArgumentException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java Wget <url> <speed>");
        }

        int speed;
        try {
            speed = Integer.parseInt(args[1]);
            if (speed <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Speed must be a positive integer.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            validateArguments(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}