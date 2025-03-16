package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Spritesheet;
import org.joml.Vector4f;

public abstract class LiquidTile extends AnimatedTile {

    private static final int LIQUID_OFFSET = Spritesheet.FONT_TEXTURE_NUM + 1;
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
