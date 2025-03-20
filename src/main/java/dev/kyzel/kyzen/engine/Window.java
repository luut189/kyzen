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
    private int currentFPS = 0;
    private int currentTick = 0;
    private int targetFPS;

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

    public int getCurrentFPS() {
        return currentFPS;
    }

    public int getCurrentTick() {
        return currentTick;
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

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null : "Failed to get primary monitor's mode";
        targetFPS = vidMode.refreshRate();

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
        int tickPerSecond = targetFPS;
        double updateInterval = 1000000000.0 / tickPerSecond;
        double frameInterval = 1000000000.0 / targetFPS;
        double delta = 0;
        long lastUpdateTime = System.nanoTime();
        long lastFrameTime = System.nanoTime();

        long timer = System.currentTimeMillis();
        int frameCount = 0;
        int tickCount = 0;

        while (!glfwWindowShouldClose(glfwWindow)) {
            long now = System.nanoTime();
            delta += (now - lastUpdateTime) / updateInterval;
            lastUpdateTime = now;

            glfwPollEvents();

            // preventing spiral of death
            if (delta > 5) delta = 5;

            // handle debug toggle
            if (ControlHandler.DEBUG_ON.pressed()) {
                Debug.setDebug(true);
            } else if (ControlHandler.DEBUG_OFF.pressed()) {
                Debug.setDebug(false);
            }

            // updating the scene with tick per second
            while (delta >= 1) {
                float deltaTime = 1.0f / tickPerSecond;
                SceneManager.updateScene(deltaTime);
                delta--;
                tickCount++;
            }

            // rendering the scene with frame per second
            if (now - lastFrameTime >= frameInterval) {
                SceneManager.renderScene();
                frameCount++;
                lastFrameTime = now;

                MouseListener.endFrame();
                glfwSwapBuffers(glfwWindow);
            }

            // updating the current meters (FPS, TPS)
            if (System.currentTimeMillis() - timer >= 1000) {
                currentTick = tickCount;
                currentFPS = frameCount;
                Debug.log("FPS: " + frameCount + " | TPS: " + tickCount);
                frameCount = 0;
                tickCount = 0;
                timer += 1000;
            }
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
