package dev.kyzel.engine.components;

import dev.kyzel.engine.Component;
import org.joml.Vector3f;

public class LightComponent extends Component {
    public float radius;
    public Vector3f color;
    public float intensity;

    public LightComponent(float radius, Vector3f color, float intensity) {
        this.radius = radius;
        this.color = color;
        this.intensity = intensity;
    }

    @Override
    public void update(float deltaTime) {

    }

}
