package dev.kyzel.kyzen.utils;

public class Debug {
    private static boolean isDebug = false;
    private static float currentDelta = 0f;

    public static void setDebug(boolean value) {
        if (isDebug == value) return;
        isDebug = value;
        System.out.println("Debug is " + (isDebug ? "on" : "off") + " now");
    }

    public static void toggleDebug() {
        isDebug = !isDebug;
        System.out.println("Debug is " + (isDebug ? "on" : "off") + " now");
    }

    public static <T> void log(T value, float deltaTime) {
        log(String.valueOf(value), deltaTime);
    }

    public static void log(String message, float deltaTime) {
        currentDelta += deltaTime;
        float printInterval = 1f;
        if (isDebug && currentDelta > printInterval) {
            System.out.println("[DEBUG]: " + message);
            currentDelta = 0;
        }
    }

    public static <T> void log(T value) {
        log(String.valueOf(value));
    }

    public static void log(String message) {
        if (isDebug) {
            System.out.println("[DEBUG]: " + message);
        }
    }
}
