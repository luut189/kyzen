package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.gfx.LightRenderer;
import dev.kyzel.kyzen.input.ControlHandler;
import dev.kyzel.kyzen.input.KeyListener;
import dev.kyzel.kyzen.input.MouseListener;
import dev.kyzel.kyzen.utils.AssetManager;
import dev.kyzel.kyzen.utils.Debug;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    private int width, height, vpX, vpY;
    private final String title;
    private long glfwWindow;
    private float targetAspectRatio = 16f / 9f;
    private float currentFPS = 0f;

    private static Window window = null;

    private Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public static Window get(int width, int height, String title) {
        if (window == null) {
            Window.window = new Window(width, height, title);
        }
        return window;
    }

    public static Window get() {
        if (window == null) {
            throw new RuntimeException("Window not initialized");
        }
        return window;
    }

    public static long getWindow() {
        if (window == null) {
            throw new RuntimeException("Window not initialized");
        }
        return get().glfwWindow;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVpX() {
        return vpX;
    }

    public int getVpY() {
        return vpY;
    }

    public float getCurrentFPS() {
        return currentFPS;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion());
        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glfwSetFramebufferSizeCallback(glfwWindow, this::windowSizeCallback);

        int[] targetWidth = new int[1];
        int[] targetHeight = new int[1];
        glfwGetFramebufferSize(glfwWindow, targetWidth, targetHeight);
        this.width = targetWidth[0];
        this.height = targetHeight[0];

        this.targetAspectRatio = (float) width / (float) height;
        glViewport(0, 0, width, height);
        AssetManager.init();

        SceneManager.changeScene(new LevelScene());
    }

    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            if (deltaTime > 0.0f) {
                if (ControlHandler.DEBUG_ON.pressed()) {
                    Debug.setDebug(true);
                } else if (ControlHandler.DEBUG_OFF.pressed()) {
                    Debug.setDebug(false);
                }
                currentFPS = 1f / deltaTime;
                Debug.log("FPS: " + currentFPS, deltaTime);
                SceneManager.updateScene(deltaTime);
                MouseListener.endFrame();
            }

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void windowSizeCallback(long window, int screenWidth, int screenHeight) {
        if (screenWidth == 0 || screenHeight == 0) {
            return;
        }

        Debug.log("Screen size: " + screenWidth + "x" + screenHeight);

        int aspectWidth = screenWidth;
        int aspectHeight = (int) ((float) aspectWidth / targetAspectRatio);
        if (aspectHeight > screenHeight) {
            aspectHeight = screenHeight;
            aspectWidth = (int) ((float) aspectHeight * targetAspectRatio);
        }
        this.width = aspectWidth;
        this.height = aspectHeight;

        // Center rectangle
        vpX = (int) (((float) screenWidth / 2f) - ((float) aspectWidth / 2f));
        vpY = (int) (((float) screenHeight / 2f) - ((float) aspectHeight / 2f));

        SceneManager.getCurrentScene().camera.adjustProjection();
        SceneManager.getCurrentScene().renderer.resize(aspectWidth, aspectHeight);
        LightRenderer.getInstance().resize(aspectWidth, aspectHeight);
    }

}
