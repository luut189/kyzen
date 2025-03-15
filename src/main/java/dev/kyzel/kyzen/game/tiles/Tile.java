package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.utils.AssetManager;

public abstract class Tile extends GameObject {

    protected Spritesheet sheet = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

    public Tile(Transform transform, float zIndex) {
        super(transform, zIndex);
    }

}
