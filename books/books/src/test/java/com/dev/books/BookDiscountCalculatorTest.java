package com.dev.books;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
public class BookDiscountCalculatorTest {

    @InjectMocks
    private BookDiscountCalculator bookDiscountCalculator;

    @Test
    public void testWithExampleInTheKata() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        bookBasket.add("Working effectively with Legacy Code");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(320.0, totalAmount);
    }

    @Test
    public void testWithNonBestOffer() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        bookBasket.add("Working effectively with Legacy Code");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(255.0, totalAmount);
    }

    @Test
    public void testWithLessBookTitleThanFive() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(210.0, totalAmount);
    }

    @Test
    public void testWithUnknownBookTitle() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        bookBasket.add("Test Driven Development by");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(260.0, totalAmount);
    }

    @Test
    public void testWithUnknownBookTitleOffer() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        bookBasket.add("Working effectively with Legacy Code");
        bookBasket.add("Working effectively with Legacy");
        bookBasket.add("Working effectively with Code");
        bookBasket.add("Working effectively Legacy Code");
        bookBasket.add("Working Legacy Code");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(505.0, totalAmount);
    }

    @Test
    public void testWithoutUnknownBookTitleOffer() {
        List<String> bookBasket = new ArrayList<>();
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Code");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Coder");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Clean Architecture");
        bookBasket.add("Test Driven Development by Example");
        bookBasket.add("Working effectively with Legacy Code");
        bookBasket.add("Working effectively with Legacy");
        bookBasket.add("Working effectively with Code");
        bookBasket.add("Working effectively Legacy Code");
        double totalAmount = bookDiscountCalculator.calculateCost(bookBasket);
        assertEquals(470.0, totalAmount);
    }
}
