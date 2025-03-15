package dev.kyzel.kyzen.engine.components;

import dev.kyzel.kyzen.engine.Component;
import org.joml.Vector3f;

public class LightComponent extends Component {
    private float radius;
    private Vector3f color;
    private float intensity;

    public LightComponent(float radius, Vector3f color, float intensity) {
        this.radius = radius;
        this.color = color;
        this.intensity = intensity;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float deltaTime) {

    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
