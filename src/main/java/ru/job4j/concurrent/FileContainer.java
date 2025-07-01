package ru.job4j.concurrent;

import java.io.File;

public final class FileContainer {
    private final File file;

    public FileContainer(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}