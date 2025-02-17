package com.ann.test.recursiveTask;

import com.ann.test.representation.ProductRepresentation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

/**
 * Load data from file to {@link com.ann.test.recursiveTask.ProductRecursiveProcessor}
 */
public class ProductRecursiveProcessor extends RecursiveTask<Void> {
    private static final int THRESHOLD = 1000;
    private final List<ProductRepresentation> products;
    private final int start, end;
    private final Map<String, String> productMap = ProductRepresentation.productsCategories;

    public ProductRecursiveProcessor(List<ProductRepresentation> products, int start, int end) {
        this.products = products;
        this.start = start;
        this.end = end;

    }

    @Override
    protected Void compute() {
        if (end - start <= THRESHOLD) {
            for (int i = start; i < end; i++) {
                ProductRepresentation product = products.get(i);
                productMap.put(product.getProductId(), product.getProductName());
            }
        } else {

            int mid = start + (end - start) / 2;
            ProductRecursiveProcessor leftTask = new ProductRecursiveProcessor(products, start, mid);
            ProductRecursiveProcessor rightTask = new ProductRecursiveProcessor(products, mid, end);


            invokeAll(leftTask, rightTask);
        }
        return null;
    }
}
