package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FifteenPercentDiscountTest {

    @Test
    void shouldApply15PercentDiscountWithThreeGrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 1));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = cereals.price().add(bread.price().multiply(BigDecimal.valueOf(2)))
                .multiply(BigDecimal.valueOf(0.85));
        // When
        var receiptAfterDiscount = discount.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(new BigDecimal("15.30"), expectedTotalPrice);
        assertEquals(1, receiptAfterDiscount.discounts().size());

    }
    @Test
    void shouldNotApply15PercentDiscountWithLessThanThreeGrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var orange = productDb.getProduct("Orange");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(orange, 2));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = bread.price().add(cereals.price())
                .add(orange.price().multiply(BigDecimal.valueOf(2)));
        // When
        var receiptAfterDiscount = discount.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
        assertEquals(3, receipt.entries().size());
    }
    @Test
    void shouldApply15PercentDiscountWithMoreThanThreeGrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 3));
        receiptEntries.add(new ReceiptEntry(cereals, 3));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var breadPrice = bread.price().multiply(BigDecimal.valueOf(3));
        var cerealPrice = cereals.price().multiply(BigDecimal.valueOf(3));
        var expectedTotalPrice = breadPrice.add(cerealPrice).multiply(BigDecimal.valueOf(0.85));
        // When
        var receiptAfterDiscount = discount.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receipt.discounts().size());
        assertEquals(2, receipt.entries().size());

    }
}
