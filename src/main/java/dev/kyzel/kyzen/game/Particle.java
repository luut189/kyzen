package dev.kyzel.kyzen.game;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LifetimeComponent;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.engine.components.VelocityComponent;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Particle extends GameObject {
    public Particle(Transform transform, float zIndex, Vector4f color, Vector2f velocity) {
        super(transform, zIndex);
        this.addComponent(new SpriteComponent(color));
        this.addComponent(new VelocityComponent(velocity));
        this.addComponent(new LightComponent(transform.scale.x, ExtendedMath.toVector3f(color), 1));
        this.addComponent(new LifetimeComponent((float) Math.random()));
    }
}
