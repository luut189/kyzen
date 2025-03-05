package dev.kyzel;

import dev.kyzel.engine.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get(1920, 1080, "Game Engine");
        window.run();
    }
}