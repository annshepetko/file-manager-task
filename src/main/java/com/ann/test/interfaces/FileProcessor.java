package com.ann.test.interfaces;

import org.springframework.core.io.InputStreamSource;

import java.nio.file.Path;

public interface FileProcessor {
    void processFile(InputStreamSource inputFile, Path outputFile);
}
