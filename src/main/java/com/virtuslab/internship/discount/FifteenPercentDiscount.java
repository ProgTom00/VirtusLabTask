package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;

public class FifteenPercentDiscount implements Discount {

    public final static String name = "FifteenPercentDiscount";

    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.85));
            var discounts = receipt.discounts();
            discounts.add(name);
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;

    }

    private boolean shouldApply(Receipt receipt) {
        int countGrainProducts = receipt.entries().stream()
                .reduce(0, (total, element) -> element
                        .product()
                        .type()
                        .compareTo(Product.Type.GRAINS) == 0 ? total + element.quantity() : total, Integer::sum);
        return countGrainProducts >= 3 && !receipt.appliedDiscount(
                TenPercentDiscount.NAME);
    }
}


