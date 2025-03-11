package dev.kyzel.engine.components;

import dev.kyzel.gfx.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private final List<Sprite> sprites;

    public Spritesheet(Texture texture, int numSprites, int spriteWidth, int spriteHeight, int spacing) {
        this.sprites = new ArrayList<>();
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i = 0; i < numSprites; i++) {
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float topY = (currentY - spriteHeight) / (float) texture.getHeight();
            float leftX = currentX / (float) texture.getWidth();
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords =  {
                new Vector2f(rightX, topY),
                new Vector2f(rightX, bottomY),
                new Vector2f(leftX, bottomY),
                new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(texture, texCoords);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
}
