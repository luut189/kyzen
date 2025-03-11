package dev.kyzel.engine;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.input.KeyListener;
import dev.kyzel.input.MouseListener;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Scene {

    protected Camera camera;
    protected List<GameObject> gameObjectList = new ArrayList<>();
    protected Renderer renderer = new Renderer();
    private boolean isRunning = false;

    public Scene() {
    }

    public void init() {

    }

    public void start() {
        for (GameObject gameObject : gameObjectList) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void update(float delta) {
        handleCameraMovement(delta);
    }

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjectList.add(gameObject);
        } else {
            gameObjectList.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public void handleCameraMovement(float delta) {
        float speed = 200f * 1 / camera.getZoom();
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            camera.position.y += delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            camera.position.y -= delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            camera.position.x -= delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            camera.position.x += delta * speed;
        }
        if (MouseListener.getScrollY() > 0) {
            camera.zoomIn();
        } else if (MouseListener.getScrollY() < 0) {
            camera.zoomOut();
        }
    }

    public Camera getCamera() {
        return camera;
    }

}
