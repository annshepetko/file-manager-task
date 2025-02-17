package com.ann.test.service;

import com.ann.test.processors.CsvProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * This service is processing incoming file and return temp file without writing on disk as a result of its work
 *
 */
@Service
public class EnrichFileService {

    private final CsvProcessor csvProcessor;

    private final static Logger logger = LoggerFactory.getLogger(EnrichFileService.class);

    public EnrichFileService(CsvProcessor csvProcessor) {
        this.csvProcessor = csvProcessor;
    }

    public Resource enrichFile(MultipartFile file) {
        return tryToEnrichFile(file);
    }

    private FileSystemResource tryToEnrichFile(MultipartFile file) {
        try {
            return processFile(file);
        } catch (IOException e) {
            logger.error("Error while file enriching at: {}", LocalDateTime.now());
            throw new RuntimeException("Error has occurred while file processing", e);
        }
    }

    private FileSystemResource processFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(file.getName(), ".csv");
        csvProcessor.processFile(file, tempFile);

        return new FileSystemResource(tempFile.toFile());
    }
}
