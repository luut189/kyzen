package dev.kyzel.kyzen.input;

import static org.lwjgl.glfw.GLFW.*;

public class ControlHandler {

    public static Control UP = new Control(GLFW_KEY_W, GLFW_KEY_UP);
    public static Control DOWN = new Control(GLFW_KEY_S, GLFW_KEY_DOWN);
    public static Control RIGHT = new Control(GLFW_KEY_D, GLFW_KEY_RIGHT);
    public static Control LEFT = new Control(GLFW_KEY_A, GLFW_KEY_LEFT);

    public static Control DEBUG_ON = new Control(GLFW_KEY_Z);
    public static Control DEBUG_OFF = new Control(GLFW_KEY_X);

}
