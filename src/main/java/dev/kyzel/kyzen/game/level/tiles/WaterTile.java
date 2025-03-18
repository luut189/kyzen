package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector4f;

public class WaterTile extends LiquidTile {

    private static final Vector4f color = ColorPalette.getDefaultRGBA(0.11f, 0.639f, 0.925f);

    public WaterTile(Transform transform, float zIndex) {
        super(transform, zIndex, color);
    }
}
