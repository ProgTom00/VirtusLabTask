package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TenPercentDiscountTest {

    @Test
    void shouldApply10PercentDiscountWhenPriceIsAbove50() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var steak = productDb.getProduct("Steak");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 1));
        receiptEntries.add(new ReceiptEntry(steak, 1));

        var receipt = new Receipt(receiptEntries);
        var discount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().add(steak.price()).multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterDiscount = discount.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldNotApply10PercentDiscountWhenPriceIsBelow50() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));

        var receipt = new Receipt(receiptEntries);
        var discount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldApply10PercentDiscountWhenPriceIsEqual50() {
        // Given
        var productDb = new ProductDb();
        var butter = productDb.getProduct("Butter");
        var cereals = productDb.getProduct("Cereals");
        var orange = productDb.getProduct("Orange");
        var apple = productDb.getProduct("Apple");

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(butter, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 3));
        receiptEntries.add(new ReceiptEntry(orange, 2));
        receiptEntries.add(new ReceiptEntry(apple, 1));

        var receipt = new Receipt(receiptEntries);
        var discount = new TenPercentDiscount();

        var butterPrice = butter.price().multiply(BigDecimal.valueOf(2));
        var cerealsPrice = cereals.price().multiply(BigDecimal.valueOf(3));
        var orangesPrice = orange.price().multiply(BigDecimal.valueOf(2));
        var applesPrice = apple.price().multiply(BigDecimal.valueOf(1));

        var totalFruitsPrice = orangesPrice.add(applesPrice);
        var butterAndCerealsPrice = cerealsPrice.add(butterPrice);
        var expectedTotalPrice = totalFruitsPrice.add(butterAndCerealsPrice).multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterDiscount = discount.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
        assertEquals(new BigDecimal("45.0"), expectedTotalPrice);
    }
}
