package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

public class DiscountServices implements Discount {

    @Override
    public Receipt apply(Receipt receipt) {

        FifteenPercentDiscount fifteenPercentDiscount = new FifteenPercentDiscount();
        TenPercentDiscount tenPercentDiscount = new TenPercentDiscount();

        receipt = fifteenPercentDiscount.apply(receipt);
        receipt = tenPercentDiscount.apply(receipt);
        return receipt;
    }
}
