package dev.kyzel.engine.components;

import dev.kyzel.engine.Component;
import dev.kyzel.engine.Transform;
import dev.kyzel.gfx.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private final Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        sprite = new Sprite(null);
        this.isDirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.isDirty = true;
    }

    @Override
    public void start() {
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float deltaTime) {
        if (!lastTransform.equals(gameObject.transform)) {
            gameObject.transform.copy(lastTransform);
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
