package dev.kyzel.kyzen.engine.components;

import dev.kyzel.kyzen.engine.Component;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.gfx.Sprite;
import dev.kyzel.kyzen.gfx.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteComponent extends Component {

    private final Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty;

    public SpriteComponent(Vector4f color) {
        this.color = color;
        sprite = new Sprite(null);
        this.isDirty = true;
    }

    public SpriteComponent(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.isDirty = true;
    }

    public SpriteComponent(Sprite sprite, Vector4f color) {
        this.sprite = sprite;
        this.color = color;
        this.isDirty = true;
    }

    @Override
    public void start() {
        lastTransform = gameObject.getTransform().copy();
    }

    @Override
    public void update(float deltaTime) {
        if (!lastTransform.equals(gameObject.getTransform())) {
            gameObject.getTransform().copy(lastTransform);
            isDirty = true;
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public Vector4f getColor() {
        return color;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        isDirty = false;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (this.color.equals(color)) return;
        this.color.set(color);
        isDirty = true;
    }
}
