package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Vector4f;

public abstract class LiquidTile extends AnimatedTile {

    private static final int LIQUID_OFFSET = 5;
    private static final int NUM_SPRITE = 3;
    private static final float[] WEIGHTS = {0.6f, 0.2f, 0.2f};

    protected Vector4f color;

    public LiquidTile(Transform transform, float zIndex, Vector4f color) {
        super(transform, zIndex, NUM_SPRITE, LIQUID_OFFSET);
        this.color = color;
        int chosenIndex = ExtendedMath.getWeightedRandom(LIQUID_OFFSET, WEIGHTS);
        this.addComponent(new SpriteComponent(sheet.getSprite(chosenIndex), color));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
