package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.engine.components.LifetimeComponent;
import dev.kyzel.kyzen.gfx.Renderer;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.input.MouseListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Scene {

    protected Camera camera;
    protected Spritesheet sheet;
    protected final List<GameObject> gameObjectList = new ArrayList<>();
    protected final Renderer renderer = new Renderer();
    protected float objectScale = 64f;

    private float currentMaxZIndex = 0;
    private boolean isRunning = false;

    public abstract void init();

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
        if (MouseListener.getScrollY() > 0) {
            camera.zoomIn();
        } else if (MouseListener.getScrollY() < 0) {
            camera.zoomOut();
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            camera.resetZoom();
        }
    }

    public float getCurrentMaxZIndex() {
        return currentMaxZIndex;
    }

    public float getObjectScale() {
        return objectScale;
    }

    public void scale(float scale) {
        this.objectScale *= scale;
        for (GameObject gameObject : gameObjectList) {
            gameObject.getTransform().scale.x *= scale;
            gameObject.getTransform().scale.y *= scale;
        }
    }

    public Spritesheet getSpritesheet() {
        return sheet;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Camera getCamera() {
        return camera;
    }

}
