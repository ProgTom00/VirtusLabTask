package com.virtuslab.internship.controller;


import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/receipt")
    public Receipt create(@RequestBody Basket basket) {
        return receiptService.totalDiscount(basket);
    }

    @GetMapping("/product/{name}")
    public Product getProduct(@RequestParam(required = false) String name) {
        return receiptService.getProduct(name);

    }
}
