package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TenAndFifteenPercentDiscountTest {


    @Test
    void shouldApplyAllDiscounts() {
        // Given
        var productDb = new ProductDb();
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cereals, 10));

        var receipt = new Receipt(receiptEntries);
        var discountServices = new DiscountServices();
        var expectedTotalPrice = cereals.price()
                .multiply(BigDecimal.valueOf(10))
                .multiply(BigDecimal.valueOf(0.85))
                .multiply(BigDecimal.valueOf(0.90));
        // When
        var receiptAfterDiscounts = discountServices.apply(receipt);
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscounts.totalPrice());
        assertEquals(2, receiptAfterDiscounts.discounts().size());
        assertEquals(1, receipt.entries().size());
    }

    @Test
    void shouldNotApplyAllDiscounts() {
        // Given
        var productDb = new ProductDb();
        var cereals = productDb.getProduct("Cereals");
        var orange = productDb.getProduct("Orange");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(orange, 2));

        var receipt = new Receipt(receiptEntries);
        var discountServices = new DiscountServices();
        var orangePrice = orange.price().multiply(BigDecimal.valueOf(2));
        var expectedTotalPrice = cereals.price().add(orangePrice);
        // When
        var receiptAfterDiscount = discountServices.apply(receipt);
        // Then
        assertEquals(0, receiptAfterDiscount.discounts().size());
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
    }

}
