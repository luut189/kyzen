package dev.kyzel.kyzen.utils;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ExtendedMath {

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

    public static Vector2f getRandomPointsInCircle(float radius) {
        return getRandomPointsInCircle(new Vector2f(), radius);
    }

    public static Vector2f getRandomPointsInCircle(Vector2f center, float radius) {
        float angle = (float) (Math.random() * 2 * Math.PI);

        radius = (float) Math.sqrt(Math.random()) * radius;

        float x = center.x + radius * Math.cos(angle);
        float y = center.y + radius * Math.sin(angle);

        return new Vector2f(x, y);
    }

    public static Vector3f toVector3f(Vector4f vector4f) {
        return new Vector3f(vector4f.x, vector4f.y, vector4f.z);
    }

    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static double pow(double a, double b) {
        return java.lang.Math.pow(a, b);
    }

}
