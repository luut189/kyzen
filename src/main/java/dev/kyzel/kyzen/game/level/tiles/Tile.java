package dev.kyzel.kyzen.game.level.tiles;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Sprite;

public abstract class Tile extends GameObject {

    protected int spriteIndex;
    protected Sprite sprite;
    protected boolean walkable = false;

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
        return walkable;
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
