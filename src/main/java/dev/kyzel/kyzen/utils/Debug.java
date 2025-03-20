package dev.kyzel.kyzen.utils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
            log(message);
            currentDelta = 0;
        }
    }

    public static <T> void log(T value) {
        log(String.valueOf(value));
    }

    public static void log(String message) {
        if (isDebug) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement caller = stackTrace.length > 3 ? stackTrace[3] : stackTrace[2];

            LocalTime date = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
            String callerInfo = caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();

            System.out.println("[" + date + "] [DEBUG] " + callerInfo + " " + message);
        }
    }
}
