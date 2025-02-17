package com.ann.test.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ann.test.processors.CsvProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrichFileServiceTest {

    @Test
    void testEnrichFile_success() throws IOException {

        CsvProcessor mockCsvProcessor = mock(CsvProcessor.class);
        EnrichFileService enrichFileService = new EnrichFileService(mockCsvProcessor);


        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "test data".getBytes());

        Path tempFilePath = Files.createTempFile("enriched_", ".csv");


        doNothing().when(mockCsvProcessor).processFile(eq(file), any(Path.class)); // Мокання виклику без повернення значення

        Resource result = enrichFileService.enrichFile(file);

        verify(mockCsvProcessor).processFile(eq(file), any(Path.class));
    }

}
