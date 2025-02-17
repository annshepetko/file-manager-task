package com.ann.test.representation;

import com.opencsv.bean.CsvBindByName;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Since the test task documentation does not specify where to store products,
 * I decided to use a Map to identify which product belongs to the trade we are receiving.
 * The Map is populated by {@link com.ann.test.loader.ProductLoader},
 * which reads data from a file every time the application starts.
 * I would use a database for this, but the task documentation does not mention anything about it.
 */
public class ProductRepresentation {
    public static volatile Map<String, String> productsCategories = new ConcurrentHashMap<>();

    @CsvBindByName(column = "productId")
    private String productId;

    @CsvBindByName(column = "productName")
    private String productName;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
