package com.ann.test.processors;

import com.ann.test.csv.handler.CsvFileHandler;
import com.ann.test.representation.ProductRepresentation;
import com.ann.test.interfaces.Validator;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvProcessorTest {

    @InjectMocks
    private CsvProcessor csvProcessor;

    @Mock
    private CsvFileHandler csvFileHandler;

    @Mock
    private Validator<String> dateValidator;

    @Mock
    private MultipartFile inputFile;

    @Mock
    private Path outputFile;

    @Mock
    private CSVReader csvReader;

    @Mock
    private CSVWriter csvWriter;

    @BeforeEach
    void setUp() throws IOException {
        // Мокимо методи csvFileHandler для створення csvReader і csvWriter
        when(csvFileHandler.createCsvReader(any())).thenReturn(csvReader);
        when(csvFileHandler.createCsvWriter(any())).thenReturn(csvWriter);
    }

    @Test
    void testProcessCsv_validFile_correctProcessing() throws IOException, CsvValidationException {
        // Підготовка тестових даних
        String[] header = {"date", "productId", "currency", "price"};
        String[] row = {"20230101", "1", "USD", "100.25"};

        when(csvReader.readNext()).thenReturn(header, row, null); // читаємо header, потім один рядок, потім null (кінець файлу)
        when(dateValidator.isValid(anyString())).thenReturn(true); // дата валідна

        // Мокимо ProductsCategories
        ProductRepresentation.productsCategories = Collections.singletonMap("1", "Product A");

        // Виклик методу
        csvProcessor.processFile(inputFile, outputFile);

        // Перевірка, що записується правильний рядок
        verify(csvWriter).writeNext(new String[]{"date", "productName", "currency", "price"});
        verify(csvWriter).writeNext(new String[]{"20230101", "Product A", "USD", "100.25"});
        verify(csvWriter).flush();
    }
    @Test
    void testProcessCsv_invalidDate_skipsRow() throws IOException, CsvValidationException {
        // Підготовка тестових даних
        String[] header = {"date", "productId", "currency", "price"};
        String[] row = {"2023011", "1", "USD", "100.25"}; // Невірна дата


        when(csvReader.readNext()).thenReturn(header, row, null);


        when(dateValidator.isValid(anyString())).thenReturn(false);

        csvProcessor.processFile(inputFile, outputFile);

        verify(csvWriter).writeNext(new String[]{"date", "productName", "currency", "price"});
        verify(csvWriter, never()).writeNext(new String[]{"2023011", "Product A", "USD", "100.25"});
    }


    @Test
    void testProcessCsv_productIdMissingInMap() throws IOException, CsvValidationException {
        // Підготовка тестових даних
        String[] header = {"date", "productId", "currency", "price"};
        String[] row = {"20230101", "100", "USD", "100.25"};

        when(csvReader.readNext()).thenReturn(header, row, null); // читаємо header, потім один рядок, потім null (кінець файлу)
        when(dateValidator.isValid(anyString())).thenReturn(true); // дата валідна


        ProductRepresentation.productsCategories = Collections.emptyMap();


        csvProcessor.processFile(inputFile, outputFile);


        verify(csvWriter).writeNext(new String[]{"date", "productName", "currency", "price"});
        verify(csvWriter).writeNext(new String[]{"20230101", "Missing Product Name", "USD", "100.25"});
    }

    @Test
    void testProcessCsv_emptyFile_throwsException() throws IOException, CsvValidationException {
        when(csvReader.readNext()).thenReturn(null); // Порожній файл

        assertThrows(RuntimeException.class, () -> {
            csvProcessor.processFile(inputFile, outputFile);
        });
    }
}
