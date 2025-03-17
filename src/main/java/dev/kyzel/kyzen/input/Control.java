package dev.kyzel.kyzen.input;

public class Control {
    private final int[] keys;

    public Control(int... keys) {
        this.keys = keys;
    }

    public boolean pressed() {
        for (int key : keys) {
            if (KeyListener.isKeyPressed(key)) return true;
        }
        return false;
    }

    public boolean down() {
        for (int key : keys) {
            if (KeyListener.isKeyDown(key)) return true;
        }
        return false;
    }

}
