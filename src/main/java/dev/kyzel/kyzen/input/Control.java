package dev.kyzel.kyzen.input;

public class Control {
    private final int[] keys;
    private final InputListener inputType;

    public Control(InputListener inputType, int... keys) {
        this.inputType = inputType;
        this.keys = keys;
    }

    public boolean pressed() {
        for (int key : keys) {
            if (inputType.isPressed(key)) return true;
        }
        return false;
    }

    public boolean down() {
        for (int key : keys) {
            if (inputType.isDown(key)) return true;
        }
        return false;
    }

}
