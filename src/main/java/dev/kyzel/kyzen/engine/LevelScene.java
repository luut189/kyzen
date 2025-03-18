package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.entity.Player;
import dev.kyzel.kyzen.game.tiles.FloorTile;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Text;
import dev.kyzel.kyzen.gfx.TextRenderer;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    private float currentDelta;
    Text text;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        // spawn a player at the middle of the screen
        Player p = new Player(new Transform(new Vector2f(Window.get().getWidth() / 2f, Window.get().getHeight() / 2f), objectScale), 3);
        this.addGameObject(p);

        int x = 0, y = 0;
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                y += (int) objectScale;
                x = 0;
            }
            GameObject o1 = new FloorTile(new Transform(new Vector2f(x, y), objectScale), 2, ColorPalette.getDefaultRGB(1,1,0.5f));
            this.addGameObject(o1);
            x += (int) objectScale;
        }
        text = Text.create("Current FPS: " + Math.round(Window.get().getCurrentFPS()),
                        new Transform(new Vector2f(10, 10), objectScale / 2),
                        ColorPalette.getDefaultRGBA(1, 0.23f, 0.6f))
                .setBackgroundColor(ColorPalette.getDefaultRGBA(1f, 0.5f, 0.5f))
                .setFlag(TextRenderer.Flag.DOUBLED);
    }

    @Override
    public void update(float delta) {
        currentDelta += delta;
        super.update(delta);
        for (GameObject ob : gameObjectList) {
            ob.update(delta);
        }
        if (currentDelta > 1f) {
            TextRenderer.cleanup();
            text.setText("Current FPS: " + Math.round(Window.get().getCurrentFPS())).render();
            currentDelta = 0f;
        }

        this.renderer.render();
    }

}
