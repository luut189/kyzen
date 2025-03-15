package dev.kyzel.game.tile;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.gfx.Spritesheet;
import org.joml.Vector4f;

public abstract class LiquidTile extends AnimatedTile {

    private static final int LIQUID_OFFSET = Spritesheet.TILE_START_INDEX + 1;
    private static final int NUM_SPRITE = 3;

    protected Vector4f color;

    public LiquidTile(Transform transform, float zIndex, Vector4f color) {
        super(transform, zIndex, NUM_SPRITE, LIQUID_OFFSET);
        this.color = color;
        this.addComponent(new SpriteComponent(sheet.getSprite(LIQUID_OFFSET), color));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
