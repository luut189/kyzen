package dev.kyzel.game.tile;

import dev.kyzel.engine.GameObject;
import dev.kyzel.engine.Transform;

public class Tile extends GameObject {

    public Tile(String name) {
        super(name);
    }

    public Tile(String name, Transform transform, int zIndex) {
        super(name, transform, zIndex);
    }
}
