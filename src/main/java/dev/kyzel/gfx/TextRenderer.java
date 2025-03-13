package dev.kyzel.gfx;

import dev.kyzel.engine.GameObject;
import dev.kyzel.engine.SceneManager;
import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.LifetimeComponent;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TextRenderer {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String LAYOUT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890,.%?!:";
    public static final Spritesheet sheet = AssetManager.getSpritesheet("assets/textures/testing.png");

    public static void renderText(String text, Transform baseTransform, Vector4f color) {
        renderText(text, baseTransform, color, null, -1);
    }

    public static void renderText(String text, Transform baseTransform, Vector4f color, float lifetime) {
        renderText(text, baseTransform, color, null, lifetime);
    }

    public static void renderText(String text, Transform baseTransform, Vector4f color, Vector4f background) {
        renderText(text, baseTransform, color, background, -1);
    }

    public static void renderText(String text,
                                  Transform baseTransform,
                                  Vector4f color, Vector4f backgroundColor,
                                  float lifetime) {
        Vector2f position = new Vector2f(baseTransform.position);

        // in order to make this work, render text have to be called after every other game object
        int zIndex = SceneManager.getCurrentScene().getCurrentMaxZIndex() + 1;

        float charSpacing = baseTransform.scale.x * 0.1f;
        for (char c : text.toUpperCase().toCharArray()) {
            if (c == ' ') {
                position.x += baseTransform.scale.x;
                continue;
            }

            int index = LAYOUT.indexOf(c);
            if (index == -1) index = LAYOUT.indexOf('?');

            GameObject charObject = new GameObject(new Transform(new Vector2f(position), baseTransform.scale), zIndex);

            charObject.addComponent(new SpriteComponent(sheet.getSprite(index), color));

            if (lifetime > 0) {
                charObject.addComponent(new LifetimeComponent(lifetime));
            }

            SceneManager.getCurrentScene().addGameObject(charObject);

            position.x += baseTransform.scale.x + charSpacing;
        }
        if (backgroundColor != null) {
            float newScaleX = text.length() * (baseTransform.scale.x + charSpacing);
            Vector2f bgScale = new Vector2f(newScaleX, baseTransform.scale.y);
            GameObject backgroundObject = new GameObject(new Transform(baseTransform.position, bgScale), zIndex - 1);
            backgroundObject.addComponent(new SpriteComponent(backgroundColor));
            SceneManager.getCurrentScene().addGameObject(backgroundObject);
        }
    }

}
