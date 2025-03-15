package dev.kyzel.engine;

import dev.kyzel.engine.components.LifetimeComponent;
import dev.kyzel.gfx.Renderer;
import dev.kyzel.input.KeyListener;
import dev.kyzel.input.MouseListener;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Scene {

    protected Camera camera;
    protected final List<GameObject> gameObjectList = new ArrayList<>();
    protected final Renderer renderer = new Renderer();

    private float currentMaxZIndex = 0;

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
        Iterator<GameObject> iterator = gameObjectList.iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            LifetimeComponent lifetimeComponent = gameObject.getComponent(LifetimeComponent.class);
            if (lifetimeComponent != null && lifetimeComponent.isExpired()) {
                iterator.remove();
                removeGameObject(gameObject);
            }
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjectList.add(gameObject);
        } else {
            gameObjectList.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
        currentMaxZIndex = Math.max(gameObject.getzIndex(), currentMaxZIndex);
    }

    public void removeGameObject(GameObject gameObject) {
        renderer.remove(gameObject);

        gameObject.removeAllComponents();
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

    public float getCurrentMaxZIndex() {
        return currentMaxZIndex;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Camera getCamera() {
        return camera;
    }

}
