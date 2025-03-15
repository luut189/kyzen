package dev.kyzel.game.tiles;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.gfx.ColorPalette;
import dev.kyzel.gfx.Spritesheet;
import org.joml.Vector4f;

public class SandTile extends Tile {

    public SandTile(Transform transform, float zIndex) {
        super(transform, zIndex);

        int sandOffset = Spritesheet.TILE_START_INDEX + (int) (Math.random() * 2) + 1;
        Vector4f color = ColorPalette.getDefaultRGBA(0.984f, 0.725f, 0.329f);

        this.addComponent(new SpriteComponent(sheet.getSprite(sandOffset), color));
    }
}