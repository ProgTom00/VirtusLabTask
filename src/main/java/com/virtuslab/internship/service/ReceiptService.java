package com.virtuslab.internship.service;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;


@Service
public class ReceiptService {

    private final static String product = "Product ";
    private final static String message = " does not exists";
    private ProductDb productDb;

    public Receipt totalDiscount(Basket basket) {
        var receiptGenerator = new ReceiptGenerator();
        var receipt = receiptGenerator.generate(basket);

        var firstDiscount = new FifteenPercentDiscount();
        receipt = firstDiscount.apply(receipt);

        var secondDiscount = new TenPercentDiscount();
        receipt = secondDiscount.apply(receipt);
        return receipt;
    }

    public Product getProduct(String name) {
        try {
            return productDb.getProduct(name);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, product + name + message);
        }
    }
}
