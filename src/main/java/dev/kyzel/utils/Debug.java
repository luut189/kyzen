package dev.kyzel.utils;

public class Debug {
    private static boolean isDebug = false;

    public static void setDebug(boolean value) {
        if (isDebug == value) return;
        isDebug = value;
        System.out.println("Debug is " + (isDebug ? "on" : "off") + " now");
    }

    public static void toggleDebug() {
        isDebug = !isDebug;
        System.out.println("Debug is " + (isDebug ? "on" : "off") + " now");
    }

    public static void debugLog(String message) {
        if (isDebug) {
            System.out.println("[DEBUG]: " + message);
        }
    }
}
