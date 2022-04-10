package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

public interface Discount {

    Receipt apply(Receipt receipt);
}
