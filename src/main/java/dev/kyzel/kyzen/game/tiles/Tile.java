package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;

public abstract class Tile extends GameObject {
    public Tile(Transform transform, float zIndex) {
        super(transform, zIndex);
    }
}
