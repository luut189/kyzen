package dev.kyzel.game.tiles;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.LightComponent;
import dev.kyzel.engine.components.SpriteComponent;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightTile extends Tile {

    public LightTile(Transform transform, float zIndex, Vector3f color, float intensity) {
        super(transform, zIndex);
        this.addComponent(new SpriteComponent(new Vector4f(color, 0.9f)));
        this.addComponent(new LightComponent(transform.scale.x, color, intensity));
    }
}
