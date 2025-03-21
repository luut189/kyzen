package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.ParticleManager;
import dev.kyzel.kyzen.game.level.TestLevel;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Text;
import dev.kyzel.kyzen.gfx.TextRenderer;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;

public class LevelScene extends Scene {

    private float currentDelta;
    Text debugText;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        currentLevel = new TestLevel(this, 0, 0);
        debugText = TextRenderer.create("FPS: " + Window.get().getCurrentFPS() + "\nTPS: " + Window.get().getCurrentTick(),
                        new Transform(new Vector2f(), objectScale / 2),
                        ColorPalette.getDefaultRGBA(1, 0.23f, 0.6f))
                .setFlag(TextRenderer.Flag.DOUBLED)
                .setFixedPosition(true);
    }

    @Override
    public void update(float delta) {
        currentDelta += delta;
        super.update(delta);
        for (GameObject ob : gameObjectList) {
            ob.update(delta);
        }
        if (currentDelta > 1f) {
            debugText.setText("FPS: " + Window.get().getCurrentFPS() + "\nTPS: " + Window.get().getCurrentTick());
            currentDelta = 0f;
        }
        ParticleManager.addAllParticles();
    }

}
