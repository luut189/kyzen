package dev.kyzel.kyzen.input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[GLFW_KEY_LAST + 1];
    private final boolean[] keyDown = new boolean[GLFW_KEY_LAST + 1];
    private int lastKey = 0;

    private KeyListener() {
    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key >= GLFW_KEY_LAST) return;

        if (action == GLFW_PRESS) {
            get().keyDown[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyDown[key] = false;
        }

        get().keyPressed[key] = isKeyDown(key) && key != get().lastKey;
        get().lastKey = key;
    }

    public static boolean isKeyDown(int key) {
        return get().keyDown[key];
    }

    public static boolean isKeyPressed(int key) {
        return get().keyPressed[key];
    }
}
