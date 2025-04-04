package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Vector4f;

public class SandTile extends Tile {

    private static final int SAND_OFFSET_START = 5;
    private static final float[] WEIGHTS = {0.6f, 0.2f, 0.2f};

    public SandTile(Transform transform, float zIndex) {
        super(transform, zIndex);

        spriteIndex = ExtendedMath.getWeightedRandom(SAND_OFFSET_START, WEIGHTS);
        Vector4f color = ColorPalette.getDefaultRGBA(0.984f, 0.725f, 0.329f);

        this.addComponent(new SpriteComponent(sheet.getSprite(spriteIndex), color));
        isWalkable = true;
    }
}