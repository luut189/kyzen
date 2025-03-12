package dev.kyzel.engine;

import dev.kyzel.gfx.Renderer;
import dev.kyzel.input.KeyListener;
import dev.kyzel.input.MouseListener;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Scene {

    protected Camera camera;
    protected final List<GameObject> gameObjectList = new ArrayList<>();
    protected final Renderer renderer = new Renderer();
    private boolean isRunning = false;

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
        Vector2f cameraPosition = camera.getPosition();
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            cameraPosition.y += delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            cameraPosition.y -= delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            cameraPosition.x -= delta * speed;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            cameraPosition.x += delta * speed;
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
