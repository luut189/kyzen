package dev.kyzel.kyzen.engine;

public class SceneManager {
    private static Scene currentScene;

    public static void updateScene(float delta) {
        currentScene.update(delta);
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
