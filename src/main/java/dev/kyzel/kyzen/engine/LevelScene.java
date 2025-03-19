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
    Text text;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

        currentLevel = new TestLevel(this, 0, 0);
        text = Text.create("Current FPS: " + Math.round(Window.get().getCurrentFPS()),
                        new Transform(new Vector2f(), objectScale / 2),
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
        text.setTransform(
                        new Transform(SceneManager.getCurrentScene().getCamera().getPosition(), text.getTransform().scale))
                .render();
        if (currentDelta > 1f) {
            text.setText("Current FPS: " + Math.round(Window.get().getCurrentFPS())).render();
            currentDelta = 0f;
        }
        ParticleManager.addAllParticles();

        this.renderer.render();
        TextRenderer.cleanup();
    }

}
