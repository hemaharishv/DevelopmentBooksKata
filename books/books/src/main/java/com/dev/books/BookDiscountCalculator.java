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

    /**
     * Metohd to calculate total cost with discount for books in basket
     *
     * @param bookBasket - List of books in the basket
     * @return - Total cost with discount
     */
    public double calculateCost(List<String> bookBasket) {
        double totalCost = 0.0;
        Map<String, Integer> knownBookTitleCounts = new HashMap<>();
        List<String> unknownBookTitles = new ArrayList<>();
        identifyBookTitleAndCount(bookBasket, knownBookTitleCounts, unknownBookTitles);
        int highestUniqueBookCount = knownBookTitleCounts.size();
        int totalKnownTitleCount = knownBookTitleCounts.values().stream().mapToInt(count -> count).sum();
        if (totalKnownTitleCount % 2 == 0 && highestUniqueBookCount >= bestBookOfferCount) {
            totalCost = calculateCostWithBestOffer(totalKnownTitleCount, knownBookTitleCounts, totalCost);
        } else {
            totalCost = calculateCostWithOffer(highestUniqueBookCount, totalCost, knownBookTitleCounts);
        }
        int remainingTotalBookCount = unknownBookTitles.size() +
                knownBookTitleCounts.values().stream().mapToInt(count -> count).sum();
        if (remainingTotalBookCount == 0) return totalCost;
        if (remainingTotalBookCount < bestBookOfferCount) return totalCost + remainingTotalBookCount * 50;
        totalCost = calculateCostForUnknownTitle(remainingTotalBookCount, totalCost);
        return totalCost;
    }

    /**
     * Method that claculates cost for unknown book titles i the basket
     *
     * @param remainingTotalBookCount - count of books whose cost to be calculated
     * @param totalCost               - total cost
     * @return total cost
     */
    private double calculateCostForUnknownTitle(int remainingTotalBookCount, double totalCost) {
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

    /**
     * Method that checks and provides total cost by identifying the non best offer for books in the basket
     *
     * @param highestUniqueBookCount - Unique known book titles available in the basket
     * @param totalCost              - Total cosr
     * @param knownBookTitleCounts   - map with known book title and their counts
     * @return
     */
    private double calculateCostWithOffer(int highestUniqueBookCount, double totalCost,
                                          Map<String, Integer> knownBookTitleCounts) {
        while (highestUniqueBookCount > 1) {
            totalCost += highestUniqueBookCount * 50 -
                    ((highestUniqueBookCount * 50) * discountAgainstUniqueCount.get(highestUniqueBookCount));
            removeBookCount(knownBookTitleCounts);
            highestUniqueBookCount = knownBookTitleCounts.size();
        }
        return totalCost;
    }

    /**
     * Method that checks and provides total cost by identifying the best offer combo of books in the basket
     *
     * @param totalKnownTitleCount - total known books count in the basket
     * @param knownBookTitleCounts - known book name and their counts map
     * @param totalCost            - total cost to be calculated
     * @return - total cost
     */
    private double calculateCostWithBestOffer(int totalKnownTitleCount, Map<String, Integer> knownBookTitleCounts,
                                              double totalCost) {
        int offerGroup = totalKnownTitleCount % bestBookOfferCount != 0 ?
                totalKnownTitleCount % bestBookOfferCount : totalKnownTitleCount / bestBookOfferCount;
        for (int i = offerGroup; i > 0; i--) {
            List<String> keys = new ArrayList<>(knownBookTitleCounts.keySet());
            int booksPerGroup = Math.min(keys.size(), bestBookOfferCount);
            if (booksPerGroup > 0) {
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
        return totalCost;
    }

    /**
     * Method to identify and form known book titles and their counts
     *
     * @param bookBasket           - basket purchased
     * @param knownBookTitleCounts - map where known book and their count in the basket stored
     * @param unknownBookTitles    - List where unknown book titles stored
     */
    private void identifyBookTitleAndCount(List<String> bookBasket, Map<String, Integer> knownBookTitleCounts,
                                           List<String> unknownBookTitles) {
        bookBasket.forEach(book -> {
            if (knownTitles.contains(book)) {
                Integer count = knownBookTitleCounts.getOrDefault(book, 0);
                knownBookTitleCounts.put(book, count + 1);
            } else {
                unknownBookTitles.add(book);
            }
        });
    }

    /**
     * Method that checks and reduces/removes count in the basket for non best offer calculation
     *
     * @param knownBookTitleCounts - known book and their counts
     */
    private void removeBookCount(Map<String, Integer> knownBookTitleCounts) {
        knownBookTitleCounts.keySet().forEach(book -> {
            if (knownBookTitleCounts.get(book) > 0) {
                knownBookTitleCounts.put(book, knownBookTitleCounts.get(book) - 1);
            }
        });
        knownBookTitleCounts.values().removeIf(value -> value == 0);
    }
}
