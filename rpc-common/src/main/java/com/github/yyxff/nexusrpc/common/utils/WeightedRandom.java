package com.github.yyxff.nexusrpc.common.utils;

import java.util.List;
import java.util.Random;

public class WeightedRandom {

    /**
     * Get random item with weights
     * Can be improved with binary search
     * @param items
     * @param weights
     * @return random item
     * @param <T>
     */
    public static <T> T select(List<T> items, List<Double> weights) {
        double total = 0;
        for (Double w : weights) {
            total += w;
        }
        double r = new Random().nextDouble(total);
        for (int i = 0; i < weights.size(); i++) {
            r -= weights.get(i);
            if (r < 0) return items.get(i);
        }
        return null;
    }
}
