package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextRenderer {

    public enum Flag {
        NONE,
        DOUBLED
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static final String LAYOUT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-_=+[],.?:;\"";
    public static final Spritesheet FONT_SHEET =
            AssetManager.getSpritesheet("assets/textures/press-start-font.png");

    private static final List<GameObject> objectBatch = new ArrayList<>();
    private static final List<Text> textToRender = new ArrayList<>();
    private static final List<RenderBatch> renderBatches = new ArrayList<>();

    public static Text create(String text, Transform transform, Vector4f color) {
        Text newText = new Text(text, transform, color);
        textToRender.add(newText);
        return newText;
    }

    public static Text create(Supplier<String> textSupplier, Transform transform, Vector4f color) {
        Text newText = new Text(textSupplier, transform, color);
        textToRender.add(newText);
        return newText;
    }

    public static void addText(Text text) {
        if (textToRender.contains(text)) return;
        textToRender.add(text);
    }

    public static void update(float delta) {
        List<Text> toRemove = new ArrayList<>();
        for (Text text : textToRender) {
            if (text.getLifetime() == -1) continue;
            text.decreaseLifetime(delta);
            if (text.getCurrentLifetime() < 0) toRemove.add(text);
        }
        textToRender.removeAll(toRemove);
    }

    public static void render() {
        for (Text text : textToRender) {
            text.render();
        }
    }

    public static void renderText(Text text) {
        renderText(text.getText(),
                text.getTransform(),
                text.getColor(), text.getBackgroundColor(),
                text.getFlag());
    }

    private static void renderText(String text,
                                   Transform baseTransform,
                                   Vector4f color, Vector4f backgroundColor,
                                   Flag flag) {
        Vector2f position = new Vector2f(baseTransform.position);

        // in order to make this work, render text have to be called after every other game object
        float zIndex = SceneManager.getCurrentScene().getCurrentMaxZIndex() + 1;

        int maxChar = 0, charCount = 0, line = 1;
        for (char c : text.toUpperCase().toCharArray()) {
            charCount++;
            if (c == ' ') {
                position.x += baseTransform.scale.x * 0.5f;
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
                        zIndex - 0.01f);
            }
            maxChar = Math.max(maxChar, charCount);

            createGameObject(
                    new Transform(new Vector2f(position), baseTransform.scale),
                    new SpriteComponent(FONT_SHEET.getSprite(index), color),
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

    public static void resize(int width, int height) {
        for (RenderBatch renderBatch : renderBatches) {
            renderBatch.resize(width, height);
        }
    }

    private static void createGameObject(Transform transform,
                                         SpriteComponent spriteComponent, float zIndex) {
        GameObject gameObject = new GameObject(transform, zIndex).addComponent(spriteComponent);
        objectBatch.add(gameObject);
        Renderer.addSpriteToBatches(spriteComponent, renderBatches);
    }

}
