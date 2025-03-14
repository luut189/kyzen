package dev.kyzel.engine;

import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.gfx.Spritesheet;
import dev.kyzel.game.tile.LightTile;
import dev.kyzel.gfx.TextRenderer;
import dev.kyzel.utils.AssetManager;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LevelScene extends Scene {

    @Override
    public void init() {
        float objectScale = 64f;
        this.camera = new Camera(new Vector2f());
        boolean testLight = true;

        Spritesheet sprites = AssetManager.getSpritesheet("assets/textures/font.png");

        int x = 0, y = 0;
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                y += 100;
                x = 0;
            }
            GameObject o1 = new GameObject(new Transform(new Vector2f(x, y), objectScale), 2);
            o1.addComponent(new SpriteComponent(sprites.getSprite(i % sprites.getNumSprites()),
                                                new Vector4f((float) Math.random(),
                                                             (float) Math.random(),
                                                             (float) Math.random(), 1f)));
            this.addGameObject(o1);
            if (testLight) {
                GameObject o = new LightTile(new Transform(new Vector2f(x, y),
                        new Vector2f(objectScale)), -1,
                        new Vector3f((float) Math.random(),
                                (float) Math.random(),
                                (float) Math.random()), 1f);
                this.addGameObject(o);
            }
            x += 100;
        }
        TextRenderer.renderText("Hello World!\n\"We are so back chat?!\n(testing)\"",
                new Transform(new Vector2f(10, 10), objectScale),
                new Vector4f(1, 0, 0, 1),
                new Vector4f(0.5f, 0.5f, 0.5f, 1f),
                 TextRenderer.Flag.DOUBLED);
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
