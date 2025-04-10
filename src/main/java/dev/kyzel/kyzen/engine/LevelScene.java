package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.level.DungeonLevel;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.TextRenderer;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        currentLevel = new DungeonLevel(this, new Vector2f(0, 0));
        TextRenderer.create(() -> "FPS: " + Window.get().getCurrentFPS() + "\nTPS: " + Window.get().getCurrentTick(),
                        new Transform(new Vector2f(0, 0), objectScale / 2),
                        ColorPalette.getDefaultRGBA(1, 0.23f, 0.6f))
                .setFlag(TextRenderer.Flag.DOUBLED)
                .setFixedPosition(true);
    }

}
