package com.ann.test.processors;

import com.ann.test.csv.handler.CsvFileHandler;
import com.ann.test.exception.exceptions.FileProcessingException;
import com.ann.test.exception.exceptions.IncorrectDataInFileException;
import com.ann.test.interfaces.FileProcessor;
import com.ann.test.interfaces.Validator;
import com.ann.test.representation.ProductRepresentation;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class process csv document according to requirements in test-task
 */

@Service
public class CsvProcessor implements FileProcessor {

    private static final Logger log = LoggerFactory.getLogger(CsvProcessor.class);
    private final CsvFileHandler csvFileHandler;
    private final Validator<String> dateValidator;

    public CsvProcessor(CsvFileHandler csvFileHandler, Validator<String> dateValidator) {
        this.csvFileHandler = csvFileHandler;
        this.dateValidator = dateValidator;
    }

    @Override
    public void processFile(InputStreamSource inputFile, Path outputFile) {

        try (CSVReader csvReader = csvFileHandler.createCsvReader(inputFile.getInputStream());
             CSVWriter csvWriter = csvFileHandler.createCsvWriter(outputFile);
        ) {
            String[] header = getHeader(csvReader);

            int productIdIndex = findColumnIndex(header, "productId");
            int dateIndex = findColumnIndex(header, "date");

            header[productIdIndex] = "productName";
            csvWriter.writeNext(header);

            String[] row;

            while ((row = csvReader.readNext()) != null) {
                if (!isRowDateValid(row[dateIndex])) {
                    continue;
                }

                row[productIdIndex] = getProductNameForId(row[productIdIndex]);
                csvWriter.writeNext(row);
            }

            csvWriter.flush();

        } catch (IOException | CsvValidationException e) {

            throw new FileProcessingException("Error while handling csv file", e);
        }
    }
    private boolean isDateValid(String[] header) {
        return isRowDateValid(header[findColumnIndex(header, "date")]);
    }


    private static String getProductNameForId(String row) {
        return ProductRepresentation.productsCategories.getOrDefault(row, "Missing Product Name");
    }

    private static String[] getHeader(CSVReader csvReader) throws IOException, CsvValidationException {
        String[] header = csvReader.readNext();
        if (header == null) {
            throw new IncorrectDataInFileException("File is empty");
        }
        return header;
    }

    private int findColumnIndex(String[] header, String columnName) {
        for (int i = 0; i < header.length; i++) {
            if (columnName.equals(header[i])) {
                return i;
            }
        }
        throw new IncorrectDataInFileException("Column " + columnName + " is not found in file");
    }

    private boolean isRowDateValid(String date) {

        return dateValidator.isValid(date);
    }
}

