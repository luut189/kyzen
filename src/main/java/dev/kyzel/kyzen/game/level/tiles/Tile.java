package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Sprite;
import org.joml.Vector4f;

public abstract class Tile extends GameObject {

    private static final int BORDER_OFFSET = 8;

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

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    // 0 - top
    // 1 - bottom
    // 2 - left
    // 3 - right
    public void setToBorder(boolean[] directions, Vector4f color) {
        if (directions[0] && directions[2]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(BORDER_OFFSET), color));
        }
        if (directions[0] && directions[3]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(BORDER_OFFSET).flipHorizontally(), color));
        }
        if (directions[1] && directions[2]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(BORDER_OFFSET).flipVertically(), color));
        }
        if (directions[1] && directions[3]) {
            this.addComponent(new SpriteComponent(sheet.getSprite(BORDER_OFFSET).flipHorizontally().flipVertically(), color));
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
