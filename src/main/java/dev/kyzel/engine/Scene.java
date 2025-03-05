package dev.kyzel.engine;

import dev.kyzel.gfx.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected List<GameObject> gameObjectList = new ArrayList<GameObject>();
    protected Renderer renderer = new Renderer();
    private boolean isRunning = false;

    public Scene() {}

    public void init() {

    }

    public void start() {
        for (GameObject gameObject : gameObjectList) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public abstract void update(float delta);

    public void addGameObject(GameObject gameObject) {
        if (!isRunning) {
            gameObjectList.add(gameObject);
        } else {
            gameObjectList.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return camera;
    }

}
