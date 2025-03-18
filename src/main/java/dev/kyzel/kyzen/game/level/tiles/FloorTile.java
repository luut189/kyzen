package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Sprite;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.utils.WeightedRandom;
import org.joml.Vector4f;

public class FloorTile extends Tile {

    private static final int FLOOR_OFFSET_START = Spritesheet.FONT_TEXTURE_NUM + 2;

    // these weights also tell the randomizer how many indices are there
    private static final float[] WEIGHTS = {0.1f, 0.1f, 0.9f};

    public FloorTile(Transform transform, float zIndex, Vector4f color) {
        super(transform, zIndex);
        spriteIndex = WeightedRandom.getWeightedRandom(FLOOR_OFFSET_START, WEIGHTS);
        Sprite chosenSprite = sheet.getSprite(spriteIndex);
        this.addComponent(
                new SpriteComponent(Math.random() > 0.5f ? chosenSprite :
                                    Math.random() > 0.2 ? chosenSprite.flipHorizontally() :
                                                          chosenSprite.flipVertically(),
                color));
        walkable = true;
    }
}
