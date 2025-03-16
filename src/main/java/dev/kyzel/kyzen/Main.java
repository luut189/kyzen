package dev.kyzel.kyzen;

import dev.kyzel.kyzen.engine.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get(1920, 1080, "Kyzen");
        window.run();
    }
}