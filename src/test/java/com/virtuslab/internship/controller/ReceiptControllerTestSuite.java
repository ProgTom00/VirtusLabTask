package com.virtuslab.internship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.DiscountServices;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import com.virtuslab.internship.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@WebMvcTest(ReceiptController.class)
public class ReceiptControllerTestSuite {

    @MockBean
    private ReceiptService receiptService;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductDb productDb = new ProductDb();
    private final Basket basket = new Basket();
    private final FifteenPercentDiscount fifteenPercentDiscount = new FifteenPercentDiscount();
    private final TenPercentDiscount tenPercentDiscount = new TenPercentDiscount();
    private final DiscountServices discountServices = new DiscountServices();

    @Test
    void shouldApply15PercentDiscountWithThreeGranProductsAndLessThan50TotalPrice() throws Exception {
        //Given
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var bread1 = productDb.getProduct("Bread");
        basket.addProduct(bread);
        basket.addProduct(bread1);
        basket.addProduct(cereals);

        var receiptEntries = List.of(new ReceiptEntry(cereals, 1, BigDecimal.valueOf(8)),
                new ReceiptEntry(bread, 2, BigDecimal.valueOf(10)));
        List<String> discounts = new ArrayList<>();
        discounts.add("FifteenPercentDiscount");

        var totalPrice = bread.price().add(cereals.price().add(bread.price()));
        var request = objectMapper.writeValueAsString(basket);
        var expectedResult = new Receipt(receiptEntries, discounts, totalPrice);

        when(receiptService.totalDiscount(basket)).thenReturn(expectedResult);

        // When && Then
        mvc.perform(MockMvcRequestBuilders.post("/v1/receipt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[0].quantity", is(expectedResult.entries().get(0).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[1].quantity", is(expectedResult.entries().get(1).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));

    }

    @Test
    void shouldApply10PercentDiscountWithTotalPriceGreaterThan50WithoutGrainProducts() throws Exception {
        // Given
        var steak = productDb.getProduct("Steak");
        var tomato = productDb.getProduct("Tomato");
        var orange = productDb.getProduct("Orange");
        basket.addProduct(steak);
        basket.addProduct(tomato);
        basket.addProduct(orange);

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(steak, 1));
        receiptEntries.add(new ReceiptEntry(tomato, 1));
        receiptEntries.add(new ReceiptEntry(orange, 1));

        List<String> discounts = new ArrayList<>();
        discounts.add("TenPercentDiscount");

        var totalPrice = steak.price().add(tomato.price().add(orange.price()));
        var request = objectMapper.writeValueAsString(basket);
        var expectedResult = new Receipt(receiptEntries, discounts, totalPrice);

        when(receiptService.totalDiscount(basket)).thenReturn(expectedResult);

        // When && Then
        mvc.perform(MockMvcRequestBuilders.post("/v1/receipt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[0].quantity", is(expectedResult.entries().get(0).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[1].quantity", is(expectedResult.entries().get(1).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));

    }

    @Test
    void shouldApplyAllDiscounts() throws Exception {
        // Given
        var cereals = productDb.getProduct("Cereals");
        basket.addProduct(cereals);

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cereals, 20));

        List<String> discounts = new ArrayList<>();
        discounts.add(String.valueOf(fifteenPercentDiscount));
        discounts.add(String.valueOf(tenPercentDiscount));

        var totalPrice = cereals.price()
                .multiply(new BigDecimal(BigInteger.valueOf(20))
                        .multiply(BigDecimal.valueOf(0.85))
                        .multiply(BigDecimal.valueOf(0.90)));

        var request = objectMapper.writeValueAsString(basket);
        var expectedResult = new Receipt(receiptEntries, discounts, totalPrice);

        var receipt = new Receipt(receiptEntries);
        var receiptAfterDiscount = discountServices.apply(receipt);

        when(receiptService.totalDiscount(basket)).thenReturn(expectedResult);

        // When && Then
        mvc.perform(MockMvcRequestBuilders.post("/v1/receipt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[0].quantity", is(expectedResult.entries().get(0).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries.size()", is(expectedResult.entries().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", hasSize(2)));
        assertEquals(totalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(new BigDecimal("122.400"), totalPrice);
    }

    @Test
    void shouldCreateReceiptWithoutDiscounts() throws Exception {
        // Given
        var orange = productDb.getProduct("Orange");
        var tomato = productDb.getProduct("Tomato");
        var butter = productDb.getProduct("Butter");
        basket.addProduct(orange);
        basket.addProduct(tomato);
        basket.addProduct(butter);

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(orange, 1));
        receiptEntries.add(new ReceiptEntry(tomato, 1));
        receiptEntries.add(new ReceiptEntry(butter, 1));

        List<String> discounts = new ArrayList<>();
        var request = objectMapper.writeValueAsString(basket);
        var totalPrice = orange.price().add(tomato.price().add(butter.price()));
        var receipt = new Receipt(receiptEntries, discounts, totalPrice);

        when(receiptService.totalDiscount(basket)).thenReturn(receipt);
        // When && Then
        mvc.perform(MockMvcRequestBuilders.post("/v1/receipt")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries[0].quantity", is(receipt.entries().get(0).quantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries.size()", is(receipt.entries().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", hasSize(0)));
        assertEquals(totalPrice, receipt.totalPrice());
    }

    @Test
    void shouldGetNameOfProduct() throws Exception {
        // Given
        var orange = productDb.getProduct("Orange");
        basket.addProduct(orange);

        when(receiptService.getProduct("Orange")).thenReturn(orange);

        // When && Then
        mvc.perform(MockMvcRequestBuilders.get("/v1/product/Orange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "Orange"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(receiptService.getProduct("Orange"), productDb.getProduct("Orange"));
        assertNotEquals(receiptService.getProduct("Steak"), productDb.getProduct("Orange"));
    }
}

