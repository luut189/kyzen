package dev.kyzel.kyzen.utils;

public class WeightedRandom {

    public static int getWeightedRandom(int start, float[] weights) {
        float totalWeight = 0;
        for (float weight : weights) {
            totalWeight += weight;
        }

        float randomValue = (float) Math.random() * totalWeight;
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
            if (randomValue < sum) {
                return start + i;
            }
        }
        return start;
    }
}
