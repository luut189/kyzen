package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.tiles.SandTile;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.TextRenderer;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    @Override
    public void init() {
        float objectScale = 64f;
        this.camera = new Camera(new Vector2f());

        int x = 0, y = 0;
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                y += (int) objectScale;
                x = 0;
            }
            GameObject o1 = new SandTile(new Transform(new Vector2f(x, y), objectScale), 2);
            this.addGameObject(o1);
            x += (int) objectScale;
        }
        TextRenderer.renderText("Hello World!\n\"We are so back chat?!\n(testing)\"",
                new Transform(new Vector2f(10, 10), objectScale),
                ColorPalette.getDefaultRGBA(0, 1, 0),
                ColorPalette.getDefaultRGBA(0.5f, 0.5f, 0.5f),
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
