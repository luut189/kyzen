package dev.kyzel.engine;

import dev.kyzel.input.KeyListener;
import dev.kyzel.input.MouseListener;
import dev.kyzel.utils.AssetManager;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    private int targetWidth, targetHeight;
    private int width, height;
    private final String title;
    private long glfwWindow;
    private float targetAspectRatio = 16f / 9f;

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

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.targetWidth = vidMode.width();
        this.targetHeight = vidMode.height();
        this.targetAspectRatio = (float) this.targetWidth / (float) this.targetHeight;

        AssetManager.getFramebuffer("default", targetWidth, targetHeight);

        SceneManager.changeScene(0);
    }

    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            if (deltaTime > 0.0f) {
//                System.out.println("FPS: " + Math.floor(1f / deltaTime));
                SceneManager.updateScene(deltaTime);
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
        this.width = screenWidth;
        this.height = screenHeight;

        System.out.println("Screen size: " + screenWidth + "x" + screenHeight);

        int aspectWidth = screenWidth;
        int aspectHeight = (int) ((float) aspectWidth / targetAspectRatio);
        if (aspectHeight > screenHeight) {
            aspectHeight = screenHeight;
            aspectWidth = (int) ((float) aspectHeight * targetAspectRatio);
        }

        // Center rectangle
        int vpX = (int) (((float) screenWidth / 2f) - ((float) aspectWidth / 2f));
        int vpY = (int) (((float) screenHeight / 2f) - ((float) aspectHeight / 2f));

        glViewport(vpX, vpY, aspectWidth, aspectHeight);

        AssetManager.resizeFramebuffer("default", aspectWidth, aspectHeight);
    }

}
