package dev.kyzel.kyzen.engine.components;

import dev.kyzel.kyzen.engine.Component;
import dev.kyzel.kyzen.engine.Transform;
import org.joml.Vector2f;

public class VelocityComponent extends Component {

    private final Vector2f velocity;

    public VelocityComponent(Vector2f velocity) {
        this.velocity = velocity;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float deltaTime) {
        if (gameObject == null) return;
        Transform transform = gameObject.getTransform();
        transform.position.y += velocity.y * deltaTime;
        transform.position.x += velocity.x * deltaTime;
    }
}
