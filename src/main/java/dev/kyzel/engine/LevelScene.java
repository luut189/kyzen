package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.game.tile.LightTile;
import dev.kyzel.utils.AssetManager;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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

        sprites = AssetManager.getSpritesheet("assets/textures/tiles.png");

        obj1 = new GameObject(new Transform(new Vector2f(100, 100), new Vector2f(objectScale)), -1);
        obj1.addComponent(new SpriteComponent(sprites.getSprite(0)));
        this.addGameObject(obj1);

        int x = 0, y = 0;
        for (int i = 0; i < sprites.getNumSprites(); i++) {
            if (i % 50 == 0) {
                y += 100;
                x = 0;
            }
            GameObject o1 = new GameObject(new Transform(new Vector2f(x, y), new Vector2f(objectScale)), -1);
            o1.addComponent(new SpriteComponent(sprites.getSprite(i % sprites.getNumSprites()),
                                                new Vector4f((float) Math.random() * 2,
                                                             (float) Math.random() * 2,
                                                             (float) Math.random() * 2, 1f)));
            this.addGameObject(o1);
//            GameObject o = new LightTile(new Transform(new Vector2f(x, y),
//                    new Vector2f(objectScale)), -1,
//                    new Vector3f((float) Math.random(),
//                            (float) Math.random(),
//                            (float) Math.random()), 1f);
//            this.addGameObject(o);
            x += 100;
        }

    }

    private void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.getShader("assets/shaders/screen.glsl");
        AssetManager.getShader("assets/shaders/blur.glsl");
        AssetManager.getShader("assets/shaders/post-process.glsl");
        AssetManager.getShader("assets/shaders/light.glsl");
        AssetManager.addSpritesheet("assets/textures/tiles.png",
                new Spritesheet(AssetManager.getTexture("assets/textures/tiles.png"), 8, 8, 0));
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
