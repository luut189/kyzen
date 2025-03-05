package dev.kyzel.engine;

public class SceneManager {
    private static Scene currentScene;

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
