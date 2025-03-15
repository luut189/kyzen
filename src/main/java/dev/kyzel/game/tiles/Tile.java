package dev.kyzel.game.tiles;

import dev.kyzel.engine.GameObject;
import dev.kyzel.engine.Transform;
import dev.kyzel.gfx.Spritesheet;
import dev.kyzel.utils.AssetManager;

public abstract class Tile extends GameObject {

    protected Spritesheet sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

    public Tile(Transform transform, float zIndex) {
        super(transform, zIndex);
    }

}
