package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.engine.components.LifetimeComponent;
import dev.kyzel.kyzen.game.level.Level;
import dev.kyzel.kyzen.gfx.Renderer;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.input.ControlHandler;
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
    protected Level currentLevel;

    private float currentMaxZIndex = 0;
    private boolean isRunning = false;

    public abstract void init();

    public void start() {
        for (GameObject gameObject : gameObjectList) {
            gameObject.start();
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

    public void render() {
        List<GameObject> added = new ArrayList<>();
        for (GameObject gameObject : gameObjectList) {
            if (camera.isNotInView(gameObject)) continue;
            this.renderer.add(gameObject);
            added.add(gameObject);
        }
        this.renderer.render();
        for (GameObject gameObject : added) {
            this.renderer.remove(gameObject);
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjectList.add(gameObject);
        } else {
            gameObjectList.add(gameObject);
            gameObject.start();
        }
        currentMaxZIndex = Math.max(gameObject.getZIndex(), currentMaxZIndex);
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
            camera.reset();
        }
        if (ControlHandler.SNAP_CAMERA_TO_PLAYER.down())
            camera.snapToPlayer(currentLevel.getPlayer(), delta);

        // Camera Dragging Function
        // WARNING: When dragging, there is a chance for the camera to get yeet very far.
        //          You can click the middle mouse button to reset the camera.
        float speed = 1.5f * objectScale * (1 / camera.getZoom());
        if (MouseListener.isDragging()) {
            camera.moveCamera(MouseListener.getDeltaX() * delta * speed, -MouseListener.getDeltaY() * delta * speed);
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

    public Level getCurrentLevel() {
        return currentLevel;
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
