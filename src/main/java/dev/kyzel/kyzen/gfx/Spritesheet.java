package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.utils.Debug;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    public static final int FONT_TEXTURE_NUM = 58;
    public static final int TILE_TEXTURE_NUM = 7;
    public static final int PLAYER_TEXTURE_NUM = 7;

    private final List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int spacing) {
        this.sprites = new ArrayList<>();

        for (int currentY = 0; currentY < texture.getHeight(); currentY += spriteHeight + spacing) {
            for (int currentX = 0; currentX < texture.getWidth(); currentX += spriteWidth + spacing) {
                if (isSpriteEmpty(texture.getPixels(),
                        currentX, currentY,
                        spriteWidth, spriteHeight,
                        texture.getWidth(), texture.getChannels())) continue;

                float leftX = currentX / (float) texture.getWidth();
                float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
                float topY = currentY / (float) texture.getHeight();
                float bottomY = (currentY + spriteHeight) / (float) texture.getHeight();

                Vector2f[] texCoords = {
                        new Vector2f(rightX, topY),
                        new Vector2f(rightX, bottomY),
                        new Vector2f(leftX, bottomY),
                        new Vector2f(leftX, topY),
                };

                Sprite sprite = new Sprite(texture, texCoords);
                sprites.add(sprite);
            }
        }
        Debug.log(sprites.size() + " sprites loaded");
    }

    private boolean isSpriteEmpty(int[] pixels, int x, int y, int width, int height, int textureWidth, int channels) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixelOffset = ((y + row) * textureWidth) + (x + col);
                int index = pixelOffset * channels;

                if (index + channels > pixels.length) {
                    continue;
                }

                int r = pixels[index];
                int g = pixels[index + 1];
                int b = pixels[index + 2];
                int a = channels == 4 ? pixels[index + 3] : 255;

                // check for specific magenta color and make sure the whole texture is not transparent
                boolean isMagenta1 = (r == 239 && g == 46 && b == 216);
                boolean isMagenta2 = (r == 189 && g == 27 && b == 204);

                if (!isMagenta1 && !isMagenta2 && a != 0) {
                    return false;
                }
            }
        }
        return true;
    }


    public int getNumSprites() {
        return sprites.size();
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
}
