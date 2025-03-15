package dev.kyzel.game.tiles;

import dev.kyzel.engine.Transform;
import dev.kyzel.gfx.ColorPalette;
import org.joml.Vector4f;

public class WaterTile extends LiquidTile {

    private static final Vector4f color = ColorPalette.getDefaultRGBA(0.11f, 0.639f, 0.925f);

    public WaterTile(Transform transform, float zIndex) {
        super(transform, zIndex, color);
    }
}
