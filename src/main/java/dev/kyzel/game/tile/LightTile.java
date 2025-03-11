package dev.kyzel.game.tile;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.LightComponent;
import org.joml.Vector3f;

public class LightTile extends Tile {

    public LightTile(Transform transform, int zIndex, Vector3f color, float intensity) {
        super(transform, zIndex);
        this.addComponent(new LightComponent(transform.scale.x, color, intensity));
    }
}
