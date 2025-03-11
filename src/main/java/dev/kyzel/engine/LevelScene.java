package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.game.tile.LightTile;
import dev.kyzel.utils.AssetManager;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
        obj1.addComponent(new SpriteComponent(sprites.getSprite(0)));
        this.addGameObject(obj1);

        for (int i = 0; i < 10; i++) {
            GameObject o = new LightTile("light" + i,
                                        new Transform(new Vector2f((i + 1) * 100, 100),
                                                      new Vector2f(objectScale)),
                                        -1,
                                        new Vector3f((float) Math.random() * i,
                                                     (float) Math.random() * i,
                                                     (float) Math.random() * i),
                                0.4f);
            this.addGameObject(o);
        }

    }

    private void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.getShader("assets/shaders/screen.glsl");
        AssetManager.getShader("assets/shaders/blur.glsl");
        AssetManager.getShader("assets/shaders/post-process.glsl");
        AssetManager.getShader("assets/shaders/light.glsl");
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
