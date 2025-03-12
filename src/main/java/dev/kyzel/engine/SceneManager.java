package dev.kyzel.engine;

public class SceneManager {
    private static final Scene[] scenes = {new LevelScene()};
    private static Scene currentScene;

    public static void updateScene(float delta) {
        currentScene.update(delta);
    }

    public static void changeScene(int newScene) {
        assert newScene >= 0 && newScene < scenes.length : "Invalid scene number";
        currentScene = scenes[newScene];
        currentScene.init();
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
