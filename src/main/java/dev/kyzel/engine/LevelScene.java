package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteRenderer;
import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelScene extends Scene {

    private GameObject obj1;
    Spritesheet sprites;

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

        GameObject obj2 = new GameObject("Object2", new Transform(new Vector2f(200, 100), new Vector2f(objectScale)), 2);
        obj2.addComponent(new SpriteRenderer(new Vector4f(1, 0, 1, 1)));
        this.addGameObject(obj2);
    }

    private void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.getShader("assets/shaders/blur.glsl");
        AssetManager.getShader("assets/shaders/post-process.glsl");
        AssetManager.addSpritesheet("assets/textures/spritesheet.png",
                new Spritesheet(AssetManager.getTexture("assets/textures/spritesheet.png"),
                        26, 16, 16, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipLeft = 0.0f;

    @Override
    public void update(float delta) {
        super.update(delta);
        spriteFlipLeft -= delta;
        if (spriteFlipLeft <= 0) {

            spriteFlipLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 1;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }
        System.out.println("FPS: " + 1f / delta);
        for (GameObject ob : gameObjectList) {
            ob.update(delta);
        }

        this.renderer.render();
    }

}
