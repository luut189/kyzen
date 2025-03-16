package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Spritesheet;
import org.joml.Vector4f;

public class SandTile extends Tile {

    public SandTile(Transform transform, float zIndex) {
        super(transform, zIndex);

        int sandOffset = Spritesheet.FONT_TEXTURE_NUM + (int) (Math.random() * 2) + 1;
        Vector4f color = ColorPalette.getDefaultRGBA(0.984f, 0.725f, 0.329f);

        this.addComponent(new SpriteComponent(sheet.getSprite(sandOffset), color));
    }
}