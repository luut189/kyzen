package dev.kyzel.engine;

public class SceneManager {
    private static Scene[] scenes = {new LevelScene()};
    private static Scene currentScene;

    public static void updateScene(float delta) {
        currentScene.update(delta);
    }

    public static void changeScene(int newScene) {
        currentScene = scenes[newScene];
        currentScene.init();
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
