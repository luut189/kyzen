package dev.kyzel.kyzen.gfx;

import org.joml.Vector2f;

public class Sprite {

    private final Texture texture;
    private final Vector2f[] texCoords;

    public Sprite(Texture texture) {
        this.texture = texture;
        this.texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Sprite flipHorizontally() {
        Vector2f[] newTexCoords = new Vector2f[texCoords.length];
        newTexCoords[3] = texCoords[0];
        newTexCoords[2] = texCoords[1];
        newTexCoords[1] = texCoords[2];
        newTexCoords[0] = texCoords[3];
        return new Sprite(texture, newTexCoords);
    }


    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }
}
