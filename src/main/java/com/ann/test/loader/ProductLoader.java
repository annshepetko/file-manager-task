package com.ann.test.loader;

import com.ann.test.recursiveTask.ProductRecursiveProcessor;
import com.ann.test.representation.ProductRepresentation;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * this class loads data from products-file into memory (Map) after application will be run
 */
@Component
public class ProductLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductLoader.class);

    @Override
    public void run(String... args) {
        String filePath = "src/main/resources/largeSizeProduct.csv";
        try (InputStream inputStream = new FileInputStream(filePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        ) {

            HeaderColumnNameMappingStrategy<ProductRepresentation> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ProductRepresentation.class);

            CsvToBean<ProductRepresentation> csvToBean = new CsvToBeanBuilder<ProductRepresentation>(bufferedReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<ProductRepresentation> productList = csvToBean.stream().toList();

            ForkJoinPool pool = new ForkJoinPool();
            ProductRecursiveProcessor task = new ProductRecursiveProcessor(productList, 0, productList.size());
            pool.invoke(task);
            pool.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error has occurred while loading products to memory at: {}", LocalDateTime.now());

        }

    }
}
