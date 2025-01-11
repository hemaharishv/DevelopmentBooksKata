package com.dev.books;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookDiscountCalculator {

    private static final List<String> knownTitles = Arrays.asList("Clean Code", "Clean Coder",
            "Clean Architecture", "Test Driven Development by Example", "Working effectively with Legacy Code");

    private static final Map<Integer, Double> discountAgainstUniqueCount = new HashMap<>() {{
        put(2, 0.05);
        put(3, 0.10);
        put(4, 0.20);
        put(5, 0.25);
    }};

    private static final int bestBookOfferCount = 4;

    public double calculateCost(List<String> bookBasket) {
        double totalCost = 0.0;
        Map<String, Integer> knownBookTitleCounts = new HashMap<>();
        List<String> unknownBookTitles = new ArrayList<>();
        bookBasket.forEach(book -> {
            if (knownTitles.contains(book)) {
                Integer count = knownBookTitleCounts.getOrDefault(book, 0);
                knownBookTitleCounts.put(book, count + 1);
            } else {
                unknownBookTitles.add(book);
            }
        });
        int highestUniqueBookCount = knownBookTitleCounts.size();
        int totalKnownTitleCount = knownBookTitleCounts.values().stream().mapToInt(count -> count).sum();
        if (totalKnownTitleCount % 2 == 0 && highestUniqueBookCount >= bestBookOfferCount) {
            int offerGroup = totalKnownTitleCount % bestBookOfferCount != 0 ?
                    totalKnownTitleCount % bestBookOfferCount : totalKnownTitleCount/bestBookOfferCount;
            for (int i = offerGroup; i > 0; i--) {
                List<String> keys = new ArrayList<>(knownBookTitleCounts.keySet());
                int booksPerGroup = Math.min(keys.size(), bestBookOfferCount);
                if(booksPerGroup > 0){
                    totalCost += booksPerGroup * 50 - ((booksPerGroup * 50)
                            * discountAgainstUniqueCount.get(booksPerGroup));
                    for (int j = 0; j < booksPerGroup; j++) {
                        if (knownBookTitleCounts.get(keys.get(j)) > 0) {
                            knownBookTitleCounts.put(keys.get(j), knownBookTitleCounts.get(keys.get(j)) - 1);
                        }
                        knownBookTitleCounts.values().removeIf(value -> value == 0);
                    }
                }
            }
        } else {
            while (highestUniqueBookCount > 1) {
                totalCost += highestUniqueBookCount * 50 -
                        ((highestUniqueBookCount * 50) * discountAgainstUniqueCount.get(highestUniqueBookCount));
                removeBookCount(knownBookTitleCounts);
                highestUniqueBookCount = knownBookTitleCounts.size();
            }
        }
        int remainingTotalBookCount = unknownBookTitles.size() +
                knownBookTitleCounts.values().stream().mapToInt(count -> count).sum();
        if (remainingTotalBookCount == 0) return totalCost;
        if (remainingTotalBookCount < bestBookOfferCount) return totalCost + remainingTotalBookCount * 50;
        while (remainingTotalBookCount > 0) {
            if (remainingTotalBookCount - bestBookOfferCount >= 0) {
                totalCost += (3 * 50) - (3 * 50 * 0.1) + 50;
                remainingTotalBookCount = remainingTotalBookCount - bestBookOfferCount;
            } else {
                totalCost += remainingTotalBookCount * 50;
                remainingTotalBookCount = 0;
            }
        }
        return totalCost;
    }

    private void removeBookCount(Map<String, Integer> knownBookTitleCounts) {
        knownBookTitleCounts.keySet().forEach(book -> {
            if (knownBookTitleCounts.get(book) > 0) {
                knownBookTitleCounts.put(book, knownBookTitleCounts.get(book) - 1);
            }
        });
        knownBookTitleCounts.values().removeIf(value -> value == 0);
    }

}
