package com.virtuslab.internship.product;

import java.math.BigDecimal;

public record Product(
        String name,
        Type type,
        BigDecimal price
) {
    public enum Type {
        DAIRY, FRUITS, VEGETABLES, MEAT, GRAINS
    }
}
