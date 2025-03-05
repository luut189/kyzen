package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteRenderer;
import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    public LevelScene() {
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f());

        Spritesheet sprites = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        GameObject obj1 = new GameObject("Object1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObject(obj1);


    }

    private void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.addSpritesheet("assets/textures/spritesheet.png",
                new Spritesheet(AssetManager.getTexture("assets/textures/spritesheet.png"),
                        26, 16, 16, 0));
    }

    @Override
    public void update(float delta) {
        System.out.println("FPS: " + 1f / delta);
        for (GameObject ob : gameObjectList) {
            ob.update(delta);
        }

        this.renderer.render();
    }
}
