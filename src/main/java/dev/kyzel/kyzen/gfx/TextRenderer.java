package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LifetimeComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.kyzel.kyzen.gfx.Renderer.MAX_BATCH_SIZE;

public class TextRenderer {

    public enum Flag {
        NONE,
        DOUBLED
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static final String LAYOUT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-_=+[],.?:;\"";
    public static final Spritesheet FONT_SHEET = AssetManager.getSpritesheet("assets/textures/spritesheet.png");

    private static final List<GameObject> objectBatch = new ArrayList<>();
    private static final List<RenderBatch> renderBatches = new ArrayList<>();

    private String text;
    private Transform transform;
    private Vector4f color;
    private Vector4f backgroundColor;
    private float lifetime;
    private Flag flag;

    private TextRenderer(String text, Transform transform, Vector4f color) {
        this.text = text;
        this.transform = transform;
        this.color = color;
        this.backgroundColor = null;
        this.lifetime = -1;
        this.flag = Flag.NONE;
    }

    public TextRenderer setText(String text) {
        this.text = text;
        return this;
    }

    public TextRenderer setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public TextRenderer setColor(Vector4f color) {
        this.color = color;
        return this;
    }

    public TextRenderer setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextRenderer setLifetime(float lifetime) {
        this.lifetime = lifetime;
        return this;
    }

    public TextRenderer setFlag(Flag flag) {
        this.flag = flag;
        return this;
    }

    public void render() {
        renderText(text, transform, color, backgroundColor, lifetime, flag);
    }

    public static TextRenderer create(String text, Transform transform, Vector4f color) {
        return new TextRenderer(text, transform, color);
    }

    private static void renderText(String text,
                                   Transform baseTransform,
                                   Vector4f color, Vector4f backgroundColor,
                                   float lifetime,
                                   Flag flag) {
        Vector2f position = new Vector2f(baseTransform.position);

        // in order to make this work, render text have to be called after every other game object
        float zIndex = SceneManager.getCurrentScene().getCurrentMaxZIndex() + 1;

        int maxChar = 0, charCount = 0, line = 1;
        for (char c : text.toUpperCase().toCharArray()) {
            charCount++;
            if (c == ' ') {
                position.x += baseTransform.scale.x;
                continue;
            }
            if (c == '\n') {
                maxChar = Math.max(maxChar, charCount);
                charCount = 0;
                line++;
                position.x = baseTransform.position.x;
                position.y -= baseTransform.scale.y;
                continue;
            }

            int index = LAYOUT.indexOf(c);
            if (index == -1) index = LAYOUT.indexOf('?');

            if (flag == Flag.DOUBLED) {
                Vector2f offset = new Vector2f(
                        position.x + baseTransform.scale.x * 0.1f,
                        position.y - baseTransform.scale.y * 0.1f
                );
                Vector4f shadowColor = new Vector4f(
                        color.x * 0.5f,
                        color.y * 0.5f,
                        color.z * 0.5f,
                        color.w
                );
                createGameObject(
                        new Transform(new Vector2f(offset), baseTransform.scale),
                        new SpriteComponent(FONT_SHEET.getSprite(index), shadowColor),
                        lifetime,
                        zIndex - 0.01f);
            }
            maxChar = Math.max(maxChar, charCount);

            createGameObject(
                    new Transform(new Vector2f(position), baseTransform.scale),
                    new SpriteComponent(FONT_SHEET.getSprite(index), color),
                    lifetime,
                    zIndex);

            position.x += baseTransform.scale.x;
        }
        if (backgroundColor != null) {
            float newScaleX = maxChar * baseTransform.scale.x;
            Vector2f bgScale = new Vector2f(newScaleX, line * baseTransform.scale.y);

            float newPositionY = baseTransform.position.y - (line - 1) * baseTransform.scale.y;
            if (flag == Flag.DOUBLED) {
                bgScale.y += baseTransform.scale.y * 0.1f;
                newPositionY -= baseTransform.scale.y * 0.1f;
            }

            createGameObject(
                    new Transform(new Vector2f(baseTransform.position.x, newPositionY), bgScale),
                    new SpriteComponent(backgroundColor),
                    lifetime,
                    zIndex - (flag == Flag.DOUBLED ? 0.02f : 0.01f));
        }
    }

    public static List<RenderBatch> getRenderBatches() {
        return renderBatches;
    }

    public static Framebuffer getFramebuffer(int i) {
        return renderBatches.get(i).getFramebuffer();
    }

    public static void renderAllTexts() {
        for (RenderBatch renderBatch : renderBatches) {
            renderBatch.render();
        }
    }

    public static void cleanup() {
        for (GameObject o : objectBatch) {
            for (RenderBatch renderBatch : renderBatches) {
                if (renderBatch.removeSprite(o.getComponent(SpriteComponent.class))) break;
            }
        }
        objectBatch.clear();
    }

    private static void createGameObject(Transform transform,
                                         SpriteComponent spriteComponent,
                                         float lifetime, float zIndex) {
        GameObject gameObject = new GameObject(transform, zIndex).addComponent(spriteComponent);
        if (lifetime > 0) gameObject.addComponent(new LifetimeComponent(lifetime));
        objectBatch.add(gameObject);
        boolean added = false;
        for (RenderBatch renderBatch : renderBatches) {
            if (renderBatch.hasRoom() && renderBatch.getzIndex() == zIndex) {
                Texture texture = spriteComponent.getTexture();
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTextureRoom())) {
                    renderBatch.addSprite(spriteComponent);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, zIndex);
            newBatch.start();
            renderBatches.add(newBatch);
            newBatch.addSprite(spriteComponent);
            Collections.sort(renderBatches);
        }
    }

}
