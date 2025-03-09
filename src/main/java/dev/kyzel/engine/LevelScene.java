package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteRenderer;
import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    private Spritesheet sprites;
    private GameObject obj1;

    public LevelScene() {
        loadResources();
    }

    @Override
    public void init() {
        float objectScale = 64f;
        this.camera = new Camera(new Vector2f());

        sprites = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        obj1 = new GameObject("Object1", new Transform(new Vector2f(100, 100), new Vector2f(objectScale)), -1);
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObject(obj1);
    }

    private void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.getShader("assets/shaders/screen.glsl");
        AssetManager.getShader("assets/shaders/blur.glsl");
        AssetManager.getShader("assets/shaders/post-process.glsl");
        AssetManager.addSpritesheet("assets/textures/spritesheet.png",
                new Spritesheet(AssetManager.getTexture("assets/textures/spritesheet.png"),
                        26, 16, 16, 0));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (GameObject ob : gameObjectList) {
            ob.update(delta);
        }

        this.renderer.render();
    }

}
