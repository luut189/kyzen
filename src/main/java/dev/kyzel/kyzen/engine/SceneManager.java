package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.gfx.TextRenderer;

public class SceneManager {
    private static Scene currentScene;

    public static void updateScene(float delta) {
        currentScene.update(delta);
    }

    public static void renderScene() {
        TextRenderer.render();
        currentScene.getRenderer().render();
        TextRenderer.cleanup();
    }

    public static void changeScene(Scene newScene) {
        currentScene = newScene;
        currentScene.init();
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
