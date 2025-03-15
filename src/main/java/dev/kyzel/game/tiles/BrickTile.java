package dev.kyzel.game.tiles;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.gfx.Spritesheet;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BrickTile extends Tile {

    private static final int BRICK_OFFSET = Spritesheet.TILE_START_INDEX;

    public BrickTile(Transform transform, float zIndex, Vector3f color) {
        super(transform, zIndex);
        this.addComponent(new SpriteComponent(sheet.getSprite(BRICK_OFFSET), new Vector4f(color, 1)));
    }
}
