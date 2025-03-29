package dev.kyzel.kyzen.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener implements InputListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private final boolean[] mouseButtonDown = new boolean[3];
    private int lastMouseButton;
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonDown.length) get().mouseButtonDown[button] = true;
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonDown.length) get().mouseButtonDown[button] = false;
        }

        get().mouseButtonPressed[button] = get().isDown(button) && button != get().lastMouseButton;
        get().lastMouseButton = button;
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().scrollX = 0;
        get().scrollY = 0;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getLastX() {
        return (float) get().lastX;
    }

    public static float getLastY() {
        return (float) get().lastY;
    }

    public static float getDeltaX() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDeltaY() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    @Override
    public boolean isPressed(int button) {
        if (button < get().mouseButtonPressed.length) return get().mouseButtonPressed[button];
        return false;
    }

    @Override
    public boolean isDown(int button) {
        if (button < get().mouseButtonDown.length) return get().mouseButtonDown[button];
        return false;
    }
}
