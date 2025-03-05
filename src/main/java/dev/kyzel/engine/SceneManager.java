package dev.kyzel.engine;

public class SceneManager {
    private static SceneManager instance;
    private static Scene currentScene;

    private SceneManager() {}

    public static SceneManager get() {
        if (SceneManager.instance == null) {
            SceneManager.instance = new SceneManager();
        }
        return SceneManager.instance;
    }

    public static void updateScene(float delta) {
        currentScene.update(delta);
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene: " + newScene;
                break;
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

}
