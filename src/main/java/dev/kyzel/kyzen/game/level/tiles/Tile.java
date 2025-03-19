package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Sprite;
import org.joml.Vector4f;

import static dev.kyzel.kyzen.gfx.Spritesheet.FONT_TEXTURE_NUM;

public abstract class Tile extends GameObject {

    protected int spriteIndex;
    protected Sprite sprite;
    protected boolean isWalkable = false;

    public Tile(Transform transform, float zIndex) {
        super(transform, zIndex);
    }

    @Override
    public void start() {
        super.start();
        SpriteComponent spriteComponent = this.getComponent(SpriteComponent.class);
        if (spriteComponent != null) {
            sprite = spriteComponent.getSprite();
        }
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    // 0 - top
    // 1 - bottom
    // 2 - left
    // 3 - right
    public void setToBorder(boolean[] directions, Vector4f color) {
        if (directions[0] && directions[2]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(FONT_TEXTURE_NUM + 8), color));
        }
        if (directions[0] && directions[3]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(FONT_TEXTURE_NUM + 8).flipHorizontally(), color));
        }
        if (directions[1] && directions[2]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(FONT_TEXTURE_NUM + 8).flipVertically(), color));
        }
        if (directions[1] && directions[3]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(FONT_TEXTURE_NUM + 8).flipHorizontally().flipVertically(), color));
        }
    }

    public Tile rotate90() {
        SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
        if (spriteComponent == null) return this;

        sprite = spriteComponent.getSprite().rotate90();
        spriteComponent.setSprite(sprite);
        return this;
    }

    public Tile flipHorizontally() {
        SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
        if (spriteComponent == null) return this;

        sprite = spriteComponent.getSprite().flipHorizontally();
        spriteComponent.setSprite(sprite);
        return this;
    }

    public Tile flipVertically() {
        SpriteComponent spriteComponent = getComponent(SpriteComponent.class);
        if (spriteComponent == null) return this;

        sprite = spriteComponent.getSprite().flipVertically();
        spriteComponent.setSprite(sprite);
        return this;
    }
}
