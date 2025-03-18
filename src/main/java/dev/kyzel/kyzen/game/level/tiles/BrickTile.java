package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Spritesheet;
import org.joml.Vector4f;

public class BrickTile extends Tile {

    private static final int BRICK_OFFSET = Spritesheet.FONT_TEXTURE_NUM;

    public BrickTile(Transform transform, float zIndex, Vector4f color, boolean corner) {
        super(transform, zIndex);
        spriteIndex = !corner ? BRICK_OFFSET : BRICK_OFFSET + 1;
        this.addComponent(new SpriteComponent(sheet.getSprite(spriteIndex), color));
    }
}
