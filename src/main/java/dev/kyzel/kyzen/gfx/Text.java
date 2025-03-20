package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.Transform;
import org.joml.Vector4f;

public class Text {

    private String text;
    private Transform transform;
    private Vector4f color;
    private Vector4f backgroundColor;
    private float lifetime;
    private TextRenderer.Flag flag;

    private Text(String text, Transform transform, Vector4f color) {
        this.text = text;
        this.transform = transform;
        this.color = color;
        this.backgroundColor = null;
        this.lifetime = -1;
        this.flag = TextRenderer.Flag.NONE;
    }

    public static Text create(String text, Transform transform, Vector4f color) {
        return new Text(text, transform, color);
    }

    public TextRenderer.Flag getFlag() {
        return flag;
    }

    public float getLifetime() {
        return lifetime;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor;
    }

    public Vector4f getColor() {
        return color;
    }

    public Transform getTransform() {
        return transform;
    }

    public String getText() {
        return text;
    }

    public Text setText(String text) {
        this.text = text;
        return this;
    }

    public Text setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public Text setColor(Vector4f color) {
        this.color = color;
        return this;
    }

    public Text setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Text setLifetime(float lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public Text setFlag(TextRenderer.Flag flag) {
        this.flag = flag;
        return this;
    }

    public void render() {
        TextRenderer.renderText(this);
    }


}
