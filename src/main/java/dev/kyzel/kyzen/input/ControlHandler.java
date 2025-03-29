package dev.kyzel.kyzen.input;

import static org.lwjgl.glfw.GLFW.*;

public class ControlHandler {

    public static Control UP = new Control(KeyListener.get(), GLFW_KEY_W, GLFW_KEY_UP);
    public static Control DOWN = new Control(KeyListener.get(), GLFW_KEY_S, GLFW_KEY_DOWN);
    public static Control RIGHT = new Control(KeyListener.get(), GLFW_KEY_D, GLFW_KEY_RIGHT);
    public static Control LEFT = new Control(KeyListener.get(), GLFW_KEY_A, GLFW_KEY_LEFT);
    public static Control PLAYER_ATTACK = new Control(MouseListener.get(), GLFW_MOUSE_BUTTON_1);

    public static Control SNAP_CAMERA_TO_PLAYER = new Control(KeyListener.get(), GLFW_KEY_SPACE);
    public static Control RESET_CAMERA = new Control(MouseListener.get(), GLFW_MOUSE_BUTTON_MIDDLE);
    public static Control DRAG = new Control(MouseListener.get(), GLFW_MOUSE_BUTTON_2);

    public static Control DEBUG_ON = new Control(KeyListener.get(), GLFW_KEY_Z);
    public static Control DEBUG_OFF = new Control(KeyListener.get(), GLFW_KEY_X);
}
