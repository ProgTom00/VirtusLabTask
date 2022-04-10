package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.DiscountServices;
import com.virtuslab.internship.product.ProductDb;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReceiptGeneratorTest {

    @Test
    void shouldGenerateReceiptForGivenBasket() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");
        var expectedTotalPrice = milk.price().multiply(BigDecimal.valueOf(2)).add(bread.price()).add(apple.price());
        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(apple);

        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(3, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(0, receipt.discounts().size());
    }

    @Test
    void shouldGenerateReceiptForEmptyBasket() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var expectedTotalPrice = BigDecimal.valueOf(0);

        var receiptGenerator = new ReceiptGenerator();
        // When
        var receipt = receiptGenerator.generate(cart);
        // Then
        assertNotNull(receipt);
        assertEquals(0, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(0, receipt.discounts().size());
    }

    @Test
    void shouldGenerateReceiptWithDiscountsForGivenBasket() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var steak = productDb.getProduct("Steak");

        var expectedTotalPrice = cereals.price().multiply(BigDecimal.valueOf(3))
                .add(steak.price())
                .multiply(BigDecimal.valueOf(0.85))
                .multiply(BigDecimal.valueOf(0.9));

        cart.addProduct(cereals);
        cart.addProduct(cereals);
        cart.addProduct(cereals);
        cart.addProduct(steak);

        var receiptGenerator = new ReceiptGenerator();
        var discount = new DiscountServices();
        // When
        var receipt = receiptGenerator.generate(cart);
        receipt = discount.apply(receipt);
        // Then
        assertEquals(2, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(2, receipt.discounts().size());

    }
}
