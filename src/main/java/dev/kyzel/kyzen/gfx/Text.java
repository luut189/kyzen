package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.Camera;
import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public class Text {

    private String text;
    private Supplier<String> textSupplier;
    private Transform transform;
    private final Transform baseTransform;
    private Vector4f color;
    private Vector4f backgroundColor;
    private float lifetime;
    private float currentLifetime;
    private TextRenderer.Flag flag;
    private boolean fixedPosition;

    private Text(Transform transform, Vector4f color) {
        this.transform = transform;
        this.baseTransform = transform.copy();
        this.color = color;
        this.backgroundColor = null;
        this.lifetime = -1;
        this.currentLifetime = -1;
        this.flag = TextRenderer.Flag.NONE;
        this.fixedPosition = false;
    }

    public Text(String text, Transform transform, Vector4f color) {
        this(transform, color);
        this.text = text;
        this.textSupplier = null;
    }

    public Text(Supplier<String> textSupplier, Transform transform, Vector4f color) {
        this(transform, color);
        this.text = null;
        this.textSupplier = textSupplier;
    }

    public TextRenderer.Flag getFlag() {
        return flag;
    }

    public float getLifetime() {
        return lifetime;
    }

    public float getCurrentLifetime() {
        return currentLifetime;
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
        return (textSupplier == null ? text : textSupplier.get());
    }

    public Text setText(String text) {
        this.text = text;
        this.textSupplier = null;
        return this;
    }

    public Text setTextSupplier(Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
        this.text = null;
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
        this.currentLifetime = lifetime;
        return this;
    }

    public void decreaseLifetime(float delta) {
        currentLifetime -= delta;
    }

    public Text setFlag(TextRenderer.Flag flag) {
        this.flag = flag;
        return this;
    }

    public Text setFixedPosition(boolean fixedPosition) {
        this.fixedPosition = fixedPosition;
        return this;
    }

    public void render() {
        // make the text viewable when placed at edge
        Transform transformToUse = baseTransform.copy();
        float xOffset = 0, yOffset = 0;

        // Adjust for Y position at screen boundaries
        if (baseTransform.position.y == 0 || baseTransform.position.y == Window.get().getHeight()) {
            int multiplier = (baseTransform.position.y == 0) ? 1 : -1;
            for (char c : getText().toCharArray()) {
                if (c == '\n') yOffset += baseTransform.scale.y * multiplier;
            }
        }

        // Adjust for X position at right screen boundary
        if (baseTransform.position.x == Window.get().getWidth()) {
            for (char c : getText().toCharArray()) {
                if (c == ' ') xOffset -= baseTransform.scale.x * 0.5f;
                else if (c == '\n') xOffset = 0;
                else xOffset -= baseTransform.scale.x;
            }
        }

        transformToUse.position.x += xOffset;
        transformToUse.position.y += yOffset;

        if (fixedPosition) {
            Camera camera = SceneManager.getCurrentScene().getCamera();
            transform.position = new Vector2f(transformToUse.position).mul(1 / camera.getZoom())
                    .add(camera.getPosition());
            transform.scale = new Vector2f(transformToUse.scale).mul(1 / camera.getZoom());
        }
        TextRenderer.renderText(this);
    }
}
