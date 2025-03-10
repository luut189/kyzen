package dev.kyzel.game.tile;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.LightComponent;
import org.joml.Vector3f;

public class LightTile extends Tile {

    public LightTile(String name, Transform transform, int zIndex, Vector3f color, float intensity) {
        super(name, transform, zIndex);
        this.addComponent(new LightComponent(transform.scale.x, color, intensity));
    }
}
