package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightTile extends Tile {

    public LightTile(Transform transform, float zIndex, Vector3f color, float intensity) {
        super(transform, zIndex);
        this.addComponent(new SpriteComponent(new Vector4f(color, 0.9f)))
            .addComponent(new LightComponent(transform.scale.x, color, intensity));
    }
}
