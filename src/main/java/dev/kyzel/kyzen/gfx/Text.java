package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.Camera;
import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public class Text {

    private String text;
    private Supplier<String> textSupplier;
    private Transform transform;
    private final Vector2f baseScale;
    private Vector4f color;
    private Vector4f backgroundColor;
    private float lifetime;
    private float currentLifetime;
    private TextRenderer.Flag flag;
    private boolean fixedPosition;

    private Text(Transform transform, Vector4f color) {
        this.transform = transform;
        this.baseScale = transform.scale;
        this.color = color;
        this.backgroundColor = null;
        this.lifetime = -1;
        this.currentLifetime = -1;
        this.flag = TextRenderer.Flag.NONE;
        this.fixedPosition = false;
    }

    private Text(String text, Transform transform, Vector4f color) {
        this(transform, color);
        this.text = text;
        this.textSupplier = null;
    }

    public Text(Supplier<String> textSupplier, Transform transform, Vector4f color) {
        this(transform, color);
        this.text = null;
        this.textSupplier = textSupplier;
    }

    public static Text create(String text, Transform transform, Vector4f color) {
        return new Text(text, transform, color);
    }

    public static Text create(Supplier<String> textSupplier, Transform transform, Vector4f color) {
        return new Text(textSupplier, transform, color);
    }

    public boolean isFixedPosition() {
        return fixedPosition;
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
        if (isFixedPosition()) {
            Camera camera = SceneManager.getCurrentScene().getCamera();
            transform.position = new Vector2f(camera.getPosition()).add(new Vector2f(0, transform.scale.y));
            transform.scale = new Vector2f(baseScale).mul(1 / camera.getZoom());
        }
        TextRenderer.renderText(this);
    }
}
