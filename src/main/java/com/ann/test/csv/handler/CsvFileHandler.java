package com.ann.test.csv.handler;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates all necessaries readers and writers
 */

@Component
public class CsvFileHandler {

    public CSVReader createCsvReader(InputStream inputStream) throws IOException {
        return new CSVReader(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
    }

    public CSVWriter createCsvWriter(Path outputFile) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8);
        return new CSVWriter(writer);
    }
}
