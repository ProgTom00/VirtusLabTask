package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiptGenerator {

    private HashMap<Product, Integer> countProductsInBasket(Basket basket) {

        HashMap<Product, Integer> productsMap = new HashMap<>();
        for (Product product : basket.getProducts()) {
            if (productsMap.containsKey(product)) {
                productsMap.merge(product, 1, Integer::sum);
            } else {
                productsMap.put(product, 1);
            }
        }
        return productsMap;
    }

    private List<ReceiptEntry> getEntries(HashMap<Product, Integer> productsMap) {

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        productsMap.forEach((product, quantity) -> {
            receiptEntries.add(new ReceiptEntry(product, quantity));
        });
        return receiptEntries;
    }

    public Receipt generate(Basket basket) {
        HashMap<Product, Integer> productsMap = countProductsInBasket(basket);
        List<ReceiptEntry> receiptEntries = getEntries(productsMap);
        return new Receipt(receiptEntries);
    }

}
