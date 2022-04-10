package com.virtuslab.internship.receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record Receipt(

        List<ReceiptEntry> entries,
        List<String> discounts,
        BigDecimal totalPrice) {

    public Receipt(List<ReceiptEntry> entries) {
        this(entries,
                new ArrayList<>(),
                entries.stream()
                        .map(ReceiptEntry::totalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    public boolean appliedDiscount(String name) {
        for (String loopDiscount : discounts) {
            if (loopDiscount.equals(name)) {
                return true;
            }
        }
        return false;
    }
}

